package pl.karollisiewicz.movie.app.data;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okhttp3.internal.http.RealResponseBody;
import pl.karollisiewicz.movie.app.ConsoleLogger;
import pl.karollisiewicz.movie.app.data.source.db.MovieDao;
import pl.karollisiewicz.movie.app.data.source.web.Movie;
import pl.karollisiewicz.movie.app.data.source.web.MovieService;
import pl.karollisiewicz.movie.app.data.source.web.MovieWebRepository;
import pl.karollisiewicz.movie.app.data.source.web.Movies;
import pl.karollisiewicz.movie.app.react.TestSchedulers;
import pl.karollisiewicz.movie.domain.MovieRepository;
import pl.karollisiewicz.movie.domain.exception.AuthorizationException;
import pl.karollisiewicz.movie.domain.exception.CommunicationException;
import retrofit2.HttpException;
import retrofit2.Response;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.karollisiewicz.movie.app.MovieMatcher.favourite;
import static pl.karollisiewicz.movie.app.MovieMatcher.hasBackDropUrl;
import static pl.karollisiewicz.movie.app.MovieMatcher.hasOverview;
import static pl.karollisiewicz.movie.app.MovieMatcher.hasPosterUrl;
import static pl.karollisiewicz.movie.app.MovieMatcher.isRated;
import static pl.karollisiewicz.movie.app.MovieMatcher.isTitled;
import static pl.karollisiewicz.movie.app.MovieMatcher.wasReleasedOn;
import static pl.karollisiewicz.movie.app.data.source.MovieFactory.aMovie;
import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.POPULARITY;

public class MovieWebRepositoryTest {
    private MovieRepository objectUnderTest;

    @Mock
    private MovieService movieService;

    @Mock
    private MovieDao movieDao;

    private List<pl.karollisiewicz.movie.domain.Movie> popularMovies;

    private Movie aMovie = aMovie();

    private Exception exception;

    @Before
    public void beforeEach() {
        initMocks(this);
        objectUnderTest = new MovieWebRepository(movieService, movieDao, new TestSchedulers(), new ConsoleLogger());
    }

    @Test
    public void whenFetchingMovies_TheyShouldBeReturned() {
        givenServiceReturningMovies();
        givenNoFavouriteMovies();

        whenFetchingPopularMovies();

        thenMovieIsReturned();
    }

    @Test
    public void whenClientIsNotAuthorized_UnauthorizedErrorIsReturned() {
        givenUnauthorizedService();
        givenNoFavouriteMovies();

        whenFetchingPopularMovies();

        thenUnauthorizedErrorIsReturned();
    }

    @Test
    public void whenHostIsUnreachable_CommunicationErrorIsReturned() {
        givenUnreachableHost();
        givenNoFavouriteMovies();

        whenFetchingPopularMovies();

        thenCommunicationErrorIsReturned();
    }

    @Test
    public void whenFetchingMoviesThatAreFavourite_ThenItShouldBeMarkedAsFavourite() {
        givenServiceReturningMovies();
        givenFavouriteMovies();

        whenFetchingPopularMovies();

        thenMovieIsReturned();
        andIsFavourite();
    }

    private void givenServiceReturningMovies() {
        when(movieService.fetchPopular()).thenReturn(
                Single.just(new Movies(Collections.singletonList(aMovie)))
        );
    }

    private void givenFavouriteMovies() {
        when(movieDao.fetchAll()).thenReturn(
            Single.just(Collections.singletonList(aMovie))
        );
        when(movieDao.fetchById(aMovie.getId())).thenReturn(
                Maybe.just(aMovie)
        );
    }

    private void givenNoFavouriteMovies() {
        when(movieDao.fetchAll()).thenReturn(Single.just(Collections.emptyList()));
        when(movieDao.fetchById(aMovie.getId())).thenReturn(Maybe.empty());
    }

    private void givenUnauthorizedService() {
        when(movieService.fetchPopular()).thenReturn(
                Single.error(new HttpException(Response.error(401, new RealResponseBody(null, null))))
        );
    }

    private void givenUnreachableHost() {
        when(movieService.fetchPopular()).thenReturn(
                Single.error(new UnknownHostException())
        );
    }

    private void whenFetchingPopularMovies() {
        try {
            popularMovies = objectUnderTest.fetchBy(POPULARITY).blockingGet();
        } catch (Exception e) {
            this.exception = e;
        }
    }

    private void thenMovieIsReturned() {
        assertThat(popularMovies, hasSize(1));
        final pl.karollisiewicz.movie.domain.Movie popularMovie = popularMovies.get(0);
        assertThat(popularMovie, allOf(asList(
                isTitled(aMovie.getTitle()),
                isRated(aMovie.getVoteAverage()),
                hasOverview(aMovie.getOverview()),
                hasPosterUrl(aMovie.getPosterPath()),
                hasBackDropUrl(aMovie.getBackdropPath()),
                wasReleasedOn(new LocalDate(2017, 1, 27))
        )));
    }

    private void andIsFavourite() {
        assertThat(popularMovies.get(0), is(favourite()));
    }

    private void thenUnauthorizedErrorIsReturned() {
        assertThat(exception, instanceOf(AuthorizationException.class));
    }

    private void thenCommunicationErrorIsReturned() {
        assertThat(exception, instanceOf(CommunicationException.class));
    }
}
