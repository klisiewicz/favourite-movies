package pl.karollisiewicz.cinema.app.movie;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import java.sql.SQLException;

import io.reactivex.Maybe;
import io.reactivex.Single;
import pl.karollisiewicz.cinema.domain.movie.MovieDetails;
import pl.karollisiewicz.cinema.domain.movie.MovieId;
import pl.karollisiewicz.cinema.domain.movie.MovieRepository;
import pl.karollisiewicz.common.ui.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.karollisiewicz.cinema.app.MovieMatcher.favourite;
import static pl.karollisiewicz.cinema.app.livedata.LiveDataTestUtil.getValue;
import static pl.karollisiewicz.common.ui.Resource.Status.ERROR;
import static pl.karollisiewicz.common.ui.Resource.Status.SUCCESS;

public class MovieDetailsViewModelTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private MovieDetailsViewModel objectUnderTest;

    @Mock
    private MovieRepository repository;

    private final MovieDetails aMovie = MovieDetails.Builder.withId(1)
            .setTitle("A title")
            .build();

    private Resource<MovieDetails> result;

    @Before
    public void beforeEach() {
        initMocks(this);

        objectUnderTest = new MovieDetailsViewModel(repository);
    }

    @Test
    public void whenAddingToFavourites_ResultIsPassedToLiveData() throws Exception {
        givenRepositoryReturningAMovie();
        givenRepositoryThatSavesSuccessfully();
        givenViewModelHoldingMovie();

        whenTogglingFavourites();

        thenResultIsSuccess();
        andMovieIsFavourite();
    }

    @Test
    public void whenAddingToFavouritesFails_MappedErrorIsPassedToLiveData() throws Exception {
        givenRepositoryReturningAMovie();
        givenRepositoryThatFailsToSave();
        givenViewModelHoldingMovie();

        whenTogglingFavourites();

        thenResultIsError();
    }

    @Test
    public void whenRemovingFavourites_ResultIsPassedToLiveData() throws Exception {
        givenRepositoryReturningFavouriteMovie();
        givenRepositoryThatSavesSuccessfully();
        givenViewModelHoldingMovie();

        whenTogglingFavourites();

        thenResultIsSuccess();
        andMovieIsNotFavourite();
    }

    private void givenRepositoryReturningAMovie() {
        when(repository.fetchBy(any(MovieId.class))).thenReturn(Maybe.just(aMovie));
    }

    private void givenRepositoryReturningFavouriteMovie() {
        aMovie.favourite();
        givenRepositoryReturningAMovie();
    }

    private void givenRepositoryThatSavesSuccessfully() {
        when(repository.save(any(MovieDetails.class))).thenReturn(Single.just(aMovie));
    }

    private void givenRepositoryThatFailsToSave() {
        when(repository.save(any(MovieDetails.class))).thenReturn(Single.error(new SQLException()));
    }

    private void givenViewModelHoldingMovie() {
        objectUnderTest.getMovieDetails(MovieId.of(1L));
    }

    private void whenTogglingFavourites() {
        objectUnderTest.toggleFavourite();
    }

    private void thenResultIsSuccess() throws Exception {
        result = getValue(objectUnderTest.getMovieDetails(aMovie.getId()));
        assertThat(result.getStatus(), is(SUCCESS));
    }

    private void thenResultIsError() throws Exception {
        result = getValue(objectUnderTest.getMovieDetails(aMovie.getId()));
        assertThat(result.getStatus(), is(ERROR));
    }

    private void andMovieIsFavourite() {
        assertThat(result.getData(), is(favourite()));
    }

    private void andMovieIsNotFavourite() {
        assertThat(result.getData(), is(not(favourite())));
    }
}
