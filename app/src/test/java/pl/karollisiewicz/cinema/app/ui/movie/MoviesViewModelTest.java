package pl.karollisiewicz.cinema.app.ui.movie;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import pl.karollisiewicz.cinema.domain.movie.Movie;
import pl.karollisiewicz.cinema.domain.movie.MovieRepository;
import pl.karollisiewicz.common.ui.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.karollisiewicz.cinema.app.livedata.LiveDataTestUtil.getValue;
import static pl.karollisiewicz.cinema.domain.movie.MovieRepository.Criterion.POPULARITY;
import static pl.karollisiewicz.common.ui.Resource.Status.ERROR;
import static pl.karollisiewicz.common.ui.Resource.Status.LOADING;
import static pl.karollisiewicz.common.ui.Resource.Status.SUCCESS;

public class MoviesViewModelTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private MoviesViewModel objectUnderTest;

    @Mock
    private MovieRepository repository;

    private Movie aMovie;
    private Resource<List<Movie>> result;

    @Before
    public void beforeEach() {
        initMocks(this);

        objectUnderTest = new MoviesViewModel(repository);
        aMovie = new Movie.Builder(1L).setTitle("A title").build();
    }

    @Test
    public void whenFetchingIsSuccessful_ResultsArePassedToLiveData() throws Exception {
        givenRepositoryReturningAMovie();

        whenFetchingMovies();

        thenResultIsSuccess();
        thenResultContainsData();
    }

    @Test
    public void whenFetchingFails_ErrorIsPassedToLiveData() throws Exception {
        givenRepositoryReturningAnError();

        whenFetchingMovies();

        thenResultIsError();
        thenResultContainsNoData();
    }

    @Test
    public void whenFetchingDoesNotComplete_LoadingsPassedToLiveData() throws Exception {
        givenRepositoryReturningNothing();

        whenFetchingMovies();

        thenResultIsLoading();
        thenResultContainsNoData();
    }

    private void givenRepositoryReturningAMovie() {
        when(repository.fetchBy(POPULARITY)).thenReturn(Flowable.just(Collections.singletonList(aMovie)));
    }

    private void givenRepositoryReturningAnError() {
        when(repository.fetchBy(POPULARITY)).thenReturn(Flowable.error(SecurityException::new));
    }

    private void givenRepositoryReturningNothing() {
        when(repository.fetchBy(POPULARITY)).thenReturn(Flowable.never());
    }

    private void whenFetchingMovies() throws Exception {
        result = getValue(objectUnderTest.getMovies(POPULARITY));
    }

    private void thenResultIsSuccess() {
        assertThat(result.getStatus(), is(SUCCESS));
    }

    private void thenResultIsError() {
        assertThat(result.getStatus(), is(ERROR));
    }

    private void thenResultIsLoading() {
        assertThat(result.getStatus(), is(LOADING));
    }

    private void thenResultContainsNoData() {
        assertThat(result.getData(), is(nullValue()));
    }

    private void thenResultContainsData() {
        assertThat(result.getData(), hasSize(1));
        assertThat(result.getData(), contains(aMovie));
    }

    @After
    public void afterEach() {
        objectUnderTest.onCleared();
    }
}
