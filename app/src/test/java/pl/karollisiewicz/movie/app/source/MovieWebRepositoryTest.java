package pl.karollisiewicz.movie.app.source;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import pl.karollisiewicz.movie.app.react.TestSchedulers;
import pl.karollisiewicz.movie.domain.MovieRepository;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.karollisiewicz.movie.app.MovieMatcher.hasBackDropUrl;
import static pl.karollisiewicz.movie.app.MovieMatcher.hasOverview;
import static pl.karollisiewicz.movie.app.MovieMatcher.hasPosterUrl;
import static pl.karollisiewicz.movie.app.MovieMatcher.isRated;
import static pl.karollisiewicz.movie.app.MovieMatcher.isTitled;
import static pl.karollisiewicz.movie.app.MovieMatcher.wasReleasedOn;
import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.POPULARITY;

public class MovieWebRepositoryTest {
    private static final String IMAGE_URL = "http://google.com/";

    private MovieRepository objectUnderTest;

    @Mock
    private MovieService movieService;

    private List<pl.karollisiewicz.movie.domain.Movie> popularMovies;

    private Movie sampleMovie;

    @Before
    public void beforeEach() {
        initMocks(this);
        objectUnderTest = new MovieWebRepository(new MockMovieProvider(), movieService, new TestSchedulers());
        sampleMovie = createMovie();
    }

    @Test
    public void whenMoviesAreFetched_TheyShouldBeReturned() {
        givenServiceReturningMovies();

        whenFetchingPopularMovies();

        thenMoviesAreReturned();
    }

    private void givenServiceReturningMovies() {
        when(movieService.fetchPopular()).thenReturn(Single.just(new Movies(Collections.singletonList(sampleMovie))));
    }

    private void whenFetchingPopularMovies() {
        popularMovies = objectUnderTest.fetchBy(POPULARITY).blockingGet();
    }

    private void thenMoviesAreReturned() {
        assertThat(popularMovies, hasSize(1));
        final pl.karollisiewicz.movie.domain.Movie popularMovie = popularMovies.get(0);
        assertThat(popularMovie, allOf(asList(
                isTitled("Title"),
                isRated(6.66),
                hasOverview("Overview"),
                hasPosterUrl(String.format("%s%s", IMAGE_URL, sampleMovie.getPosterPath())),
                hasBackDropUrl(String.format("%s%s", IMAGE_URL, sampleMovie.getBackdropPath())),
                wasReleasedOn(new Date(2017, 1, 27))
        )));
    }

    private Movie createMovie() {
        final Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Title");
        movie.setOverview("Overview");
        movie.setVoteAverage(6.66);
        movie.setReleaseDate(new Date(2017, 1, 27));
        movie.setPosterPath("poster.jpg");
        movie.setBackdropPath("backdrop.jpg");
        return movie;
    }

    private class MockMovieProvider implements MovieImageProvider {
        @Override
        public String getPosterUrl(String resourceUrl) {
            return String.format("%s%s", IMAGE_URL, resourceUrl);
        }

        @Override
        public String getBackdropUrl(String resourceUrl) {
            return String.format("%s%s", IMAGE_URL, resourceUrl);
        }
    }
}
