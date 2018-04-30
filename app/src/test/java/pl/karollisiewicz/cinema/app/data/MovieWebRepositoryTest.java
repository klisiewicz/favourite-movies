package pl.karollisiewicz.cinema.app.data;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import pl.karollisiewicz.cinema.app.ConsoleLogger;
import pl.karollisiewicz.cinema.app.data.source.db.MovieDao;
import pl.karollisiewicz.cinema.app.data.source.web.MovieService;
import pl.karollisiewicz.cinema.app.data.source.web.MovieWebRepository;
import pl.karollisiewicz.cinema.app.data.source.web.Movies;
import pl.karollisiewicz.cinema.app.data.source.web.review.ReviewService;
import pl.karollisiewicz.cinema.app.data.source.web.review.Reviews;
import pl.karollisiewicz.cinema.app.data.source.web.video.VideoService;
import pl.karollisiewicz.cinema.app.data.source.web.video.Videos;
import pl.karollisiewicz.cinema.app.react.TestSchedulers;
import pl.karollisiewicz.cinema.domain.exception.AuthorizationException;
import pl.karollisiewicz.cinema.domain.exception.CommunicationException;
import pl.karollisiewicz.cinema.domain.movie.Movie;
import pl.karollisiewicz.cinema.domain.movie.MovieId;
import pl.karollisiewicz.cinema.domain.movie.MovieRepository;
import retrofit2.HttpException;
import retrofit2.Response;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.karollisiewicz.cinema.app.MovieMatcher.hasBackDropUrl;
import static pl.karollisiewicz.cinema.app.MovieMatcher.hasPosterUrl;
import static pl.karollisiewicz.cinema.app.MovieMatcher.isRated;
import static pl.karollisiewicz.cinema.app.MovieMatcher.isTitled;
import static pl.karollisiewicz.cinema.app.MovieMatcher.wasReleasedOn;
import static pl.karollisiewicz.cinema.app.data.source.MovieFactory.aMovie;
import static pl.karollisiewicz.cinema.app.data.source.MovieFactory.aReview;
import static pl.karollisiewicz.cinema.app.data.source.MovieFactory.aVideo;
import static pl.karollisiewicz.cinema.app.data.source.MovieFactory.favouriteMovie;
import static pl.karollisiewicz.cinema.domain.movie.MovieRepository.Criterion.POPULARITY;

public class MovieWebRepositoryTest {
    private MovieRepository objectUnderTest;

    @Mock
    private MovieService movieService;

    @Mock
    private MovieDao movieDao;

    @Mock
    private VideoService videoService;

    @Mock
    private ReviewService reviewService;

    private List<Movie> popularMovies;

    private Exception exception;

    @Before
    public void beforeEach() {
        initMocks(this);
        objectUnderTest = new MovieWebRepository(movieService, movieDao, videoService, reviewService,
                new TestSchedulers(), new ConsoleLogger()
        );
    }

    @Test
    public void whenFetchingMovies_TheyShouldBeReturned() {
        givenServiceReturningMovies();
        givenNoFavouriteMovies();
        givenNoVideos();

        whenFetchingPopularMovies();

        thenMovieIsReturned();
    }

    @Test
    public void whenClientIsNotAuthorized_UnauthorizedErrorIsReturned() {
        givenUnauthorizedService();
        givenNoFavouriteMovies();
        givenNoVideos();

        whenFetchingPopularMovies();

        thenUnauthorizedErrorIsReturned();
    }

    @Test
    public void whenHostIsUnreachable_CommunicationErrorIsReturned() {
        givenUnreachableHost();
        givenNoFavouriteMovies();
        givenNoVideos();

        whenFetchingPopularMovies();

        thenCommunicationErrorIsReturned();
    }

    private void givenServiceReturningMovies() {
        when(movieService.fetchPopular()).thenReturn(
                Single.just(new Movies(Collections.singletonList(aMovie())))
        );
    }

    private void givenFavouriteMovies() {
        when(movieDao.fetchAll()).thenReturn(Single.just(Collections.singletonList(favouriteMovie())));
        when(movieDao.fetchFavourites()).thenReturn(Single.just(Collections.singletonList(favouriteMovie())));
    }

    private void givenNoFavouriteMovies() {
        when(movieDao.fetchFavourites()).thenReturn(Single.just(Collections.emptyList()));
        when(movieDao.fetchAll()).thenReturn(Single.just(Collections.emptyList()));
    }

    private void givenNoVideos() {
        when(videoService.fetchBy(anyString())).thenReturn(Single.never());
    }

    private void givenServiceReturningVideos() {
        when(videoService.fetchBy(anyString())).thenReturn(Single.just(
                new Videos(Collections.singletonList(aVideo()))
        ));
    }

    private void givenServiceReturningReviews() {
        when(reviewService.fetchBy(anyString())).thenReturn(Single.just(
                new Reviews(Collections.singletonList(aReview()))
        ));
    }

    private void givenUnauthorizedService() {
        when(movieService.fetchPopular()).thenReturn(
                Single.error(new HttpException(Response.error(401, new RealResponseBody(
                        null, 0, new Buffer()))))
        );
    }

    private void givenUnreachableHost() {
        when(movieService.fetchPopular()).thenReturn(Single.error(new UnknownHostException()));
    }

    private void whenFetchingPopularMovies() {
        try {
            popularMovies = objectUnderTest.fetchBy(POPULARITY).blockingGet();
        } catch (Exception e) {
            this.exception = e;
        }
    }

    private void whenFetchingMovieById() {
        try {
            objectUnderTest.fetchBy(MovieId.of(1L));
        } catch (Exception e) {
            this.exception = e;
        }
    }

    private void thenMovieIsReturned() {
        assertThat(popularMovies, hasSize(1));
        final Movie popularMovie = popularMovies.get(0);
        assertThat(popularMovie, allOf(asList(
                isTitled("Title"),
                isRated(6.66),
                hasPosterUrl("poster.jpg"),
                hasBackDropUrl("backdrop.jpg"),
                wasReleasedOn(new LocalDate(2017, 1, 27))
        )));
    }

    private void thenUnauthorizedErrorIsReturned() {
        assertThat(exception, instanceOf(AuthorizationException.class));
    }

    private void thenCommunicationErrorIsReturned() {
        assertThat(exception, instanceOf(CommunicationException.class));
    }
}
