package pl.karollisiewicz.movie.app;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import java.sql.SQLException;

import io.reactivex.Single;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.karollisiewicz.movie.app.MovieMatcher.favourite;
import static pl.karollisiewicz.movie.app.Resource.Status.ERROR;
import static pl.karollisiewicz.movie.app.Resource.Status.SUCCESS;
import static pl.karollisiewicz.movie.app.livedata.LiveDataTestUtil.getValue;

public class MovieDetailsViewModelTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private MovieDetailsViewModel objectUnderTest;

    @Mock
    private MovieRepository repository;

    private final Movie aMovie = new Movie.Builder(1L)
            .setTitle("A title")
            .build();

    private Resource<Movie> result;

    @Before
    public void beforeEach() {
        initMocks(this);

        objectUnderTest = new MovieDetailsViewModel(repository);
    }

    @Test
    public void whenAddingToFavourites_ResultIsPassedToLiveData() throws Exception {
        givenRepositoryThatSavesSuccessfully();

        whenAddingToFavourites();

        thenResultIsSuccess();
        andMovieIsFavourite();
    }

    @Test
    public void whenAddingToFavouritesFails_MappedErrorIsPassedToLiveData() throws Exception {
        givenRepositoryThatFailsToSave();

        whenAddingToFavourites();

        thenResultIsError();
    }

    @Test
    public void whenRemovingFavourites_ResultIsPassedToLiveData() throws Exception {
        givenRepositoryThatSavesSuccessfully();

        whenRemovingFromFavourites();

        thenResultIsSuccess();
        andMovieIsNotFavourite();
    }

    private void givenRepositoryThatSavesSuccessfully() {
        when(repository.save(any(Movie.class))).thenReturn(Single.just(aMovie));
    }

    private void givenRepositoryThatFailsToSave() {
        when(repository.save(any(Movie.class))).thenReturn(Single.error(new SQLException()));
    }

    private void whenAddingToFavourites() {
        objectUnderTest.addToFavourites(aMovie);
    }

    private void whenRemovingFromFavourites() {
        objectUnderTest.removeFromFavourites(aMovie);
    }

    private void thenResultIsSuccess() throws Exception {
        result = getValue(objectUnderTest.getMovie());
        assertThat(result.getStatus(), is(SUCCESS));
    }

    private void thenResultIsError() throws Exception {
        result = getValue(objectUnderTest.getMovie());
        assertThat(result.getStatus(), is(ERROR));
    }

    private void andMovieIsFavourite() {
        assertThat(result.getData(), is(favourite()));
    }

    private void andMovieIsNotFavourite() {
        assertThat(result.getData(), is(not(favourite())));
    }
}
