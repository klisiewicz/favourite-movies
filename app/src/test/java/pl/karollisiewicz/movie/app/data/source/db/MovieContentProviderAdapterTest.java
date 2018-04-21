package pl.karollisiewicz.movie.app.data.source.db;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collection;

import io.reactivex.observers.TestObserver;
import pl.karollisiewicz.movie.app.data.source.web.Movie;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.BACKDROP_PATH;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.ID;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.OVERVIEW;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.POSTER_PATH;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.RELEASE_DATE;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.TITLE;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.VOTE_AVERAGE;

public class MovieContentProviderAdapterTest {
    private MovieContentProviderAdapter objectUnderTest;

    @Mock
    private ContentResolver contentResolver;

    @Mock
    private MovieContentUriProvider uriProvider;

    @Mock
    private MovieContentValuesProvider contentValuesProvider;

    private final TestObserver<Movie> singleMovieObserver = new TestObserver<>();
    private final TestObserver<Collection<Movie>> moviesObserver = new TestObserver<>();

    private Movie movie;

    @Before
    public void setup() {
        initMocks(this);

        objectUnderTest = new MovieContentProviderAdapter(contentResolver, uriProvider, contentValuesProvider);
        when(contentValuesProvider.createFrom(any())).thenReturn(mock(ContentValues.class));
    }

    @Test
    public void whenDeleteCompletes_ThenCompletesWithNoErrors() {
        givenMovie();

        whenDeletingMovie();

        thenCompletesNormally();
    }

    @Test
    public void whenDeleteFails_ThenCompletesWithError() {
        givenMovie();
        givenFailingContentResolver();

        whenDeletingMovie();

        thenCompletesWithError();
    }

    @Test
    public void whenSavingCompletes_ThenSavedMovieIsReturned() {
        givenMovie();

        whenSavingMovie();

        thenMovieIsSaved();
    }

    @Test
    public void whenSavingFails_ThenErrorIsPassedThObserver() {
        givenMovie();
        givenFailingContentResolver();

        whenSavingMovie();

        thenCompletesWithError();
    }

    @Test
    public void whenFetchingSingeFails_ThenErrorIsPassedThObserver() {
        givenFailingContentResolver();

        whenFetchingMovieById();

        thenCompletesWithError();
    }

    @Test
    public void whenNullCursorIsReturned_ThenNoResultIsPassedToTheObserver() {
        givenContentResolverProvidingNullValues();

        whenFetchingMovieById();

        thenResultIsEmpty();
    }

    @Test
    public void whenEmptyCursorIsReturned_ThenNoResultIsPassedToTheObserver() {
        givenContentResolverProvidingEmptyCursor();

        whenFetchingMovieById();

        thenResultIsEmpty();
    }

    @Test
    public void whenCursorContainingSingleItemReturned_ItsConvertedIntoAMovie() {
        givenContentResolverProvidingCursorWithSingleItem();

        whenFetchingMovieById();

        thenResultContainsASingleMovie();
    }

    @Test
    public void whenNullCursorIsReturned_ThenResultIsEmpty() {
        givenContentResolverProvidingNullValues();

        whenFetchingAllMovies();

        thenResultContainsEmptyList();
    }

    @Test
    public void whenEmptyCursorIsEmpty_ThenResultIsEmpty() {
        givenContentResolverProvidingEmptyCursor();

        whenFetchingAllMovies();

        thenResultContainsEmptyList();
    }

    @Test
    public void whenCursorContainsSingleItem_ItsConvertedIntoAMovies() {
        givenContentResolverProvidingCursorWithSingleItem();

        whenFetchingAllMovies();

        thenResultContainsListWithSingleMovie();
    }

    private void givenMovie() {
        movie = new Movie();
        movie.setId(1L);
    }

    private void givenFailingContentResolver() {
        when(contentResolver.insert(any(), any())).thenThrow(new IllegalArgumentException());
        when(contentResolver.query(any(), any(), any(), any(), any())).thenThrow(new IllegalArgumentException());
        when(contentResolver.delete(any(), any(), any())).thenThrow(new IllegalArgumentException());
        when(contentResolver.update(any(), any(), any(), any())).thenThrow(new IllegalArgumentException());
    }

    private void givenContentResolverProvidingNullValues() {
        when(contentResolver.query(any(), any(), any(), any(), any())).thenReturn(null);
    }

    private void givenContentResolverProvidingEmptyCursor() {
        final Cursor emptyCursor = mock(Cursor.class);
        when(emptyCursor.getCount()).thenReturn(0);
        when(emptyCursor.moveToNext()).thenReturn(false);

        when(contentResolver.query(any(), any(), any(), any(), any())).thenReturn(emptyCursor);
    }

    private void givenContentResolverProvidingCursorWithSingleItem() {
        final Cursor cursor = mock(Cursor.class);
        when(cursor.getCount()).thenReturn(1);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.moveToNext())
                .thenReturn(true)
                .thenReturn(false);

        when(cursor.getColumnIndex(any(String.class))).thenAnswer(invocation -> {
            final String argument = invocation.getArgument(0);
            return "_id".equals(argument) ? 0 : MovieContract.MovieEntry.Column.valueOf(argument.toUpperCase()).ordinal();
            }
        );
        when(cursor.getInt(ID.ordinal())).thenReturn(1);
        when(cursor.getString(TITLE.ordinal())).thenReturn("Title");
        when(cursor.getString(OVERVIEW.ordinal())).thenReturn("Overview");
        when(cursor.getString(POSTER_PATH.ordinal())).thenReturn("poster.jpg");
        when(cursor.getString(BACKDROP_PATH.ordinal())).thenReturn("backdrop.jpg");
        when(cursor.getDouble(VOTE_AVERAGE.ordinal())).thenReturn(6.66);
        when(cursor.getLong(RELEASE_DATE.ordinal())).thenReturn(1511308800000L); //22.11.2017

        when(contentResolver.query(any(), any(), any(), any(), any())).thenReturn(cursor);
    }

    private void whenDeletingMovie() {
        objectUnderTest.delete(movie).subscribe(singleMovieObserver);
    }

    private void whenSavingMovie() {
        objectUnderTest.save(this.movie).toObservable().subscribe(singleMovieObserver);
    }

    private void whenFetchingMovieById() {
        objectUnderTest.fetchById(1).subscribe(singleMovieObserver);
    }

    private void whenFetchingAllMovies() {
        objectUnderTest.fetchAll().subscribe(moviesObserver);
    }

    private void thenCompletesNormally() {
        singleMovieObserver.assertComplete();
        singleMovieObserver.assertNoErrors();
    }

    private void thenCompletesWithError() {
        singleMovieObserver.assertNotComplete();
        singleMovieObserver.assertError(IllegalArgumentException.class);
    }

    private void thenMovieIsSaved() {
        singleMovieObserver.assertComplete();
        singleMovieObserver.assertNoErrors();
        singleMovieObserver.assertValue(movie);
    }


    private void thenResultContainsASingleMovie() {
        singleMovieObserver.assertComplete();
        singleMovieObserver.assertNoErrors();
        assertThat(singleMovieObserver.valueCount(), is(1));

        assertMovieIsValid(getFirstValue(singleMovieObserver));
    }

    private void thenResultIsEmpty() {
        singleMovieObserver.assertComplete();
        singleMovieObserver.assertNoErrors();
        singleMovieObserver.assertNoValues();
    }

    private void thenResultContainsEmptyList() {
        moviesObserver.assertComplete();
        moviesObserver.assertNoErrors();
        moviesObserver.assertValue(emptyList());
    }

    private void thenResultContainsListWithSingleMovie() {
        moviesObserver.assertComplete();
        moviesObserver.assertNoErrors();
        assertThat(moviesObserver.valueCount(), is(1));
        assertThat(getFirstValue(moviesObserver).size(), is(1));
    }

    private void assertMovieIsValid(Movie movie) {
        assertThat(movie.getId(), is(1L));
        assertThat(movie.getTitle(), is("Title"));
        assertThat(movie.getOverview(), is("Overview"));
        assertThat(movie.getPosterPath(), is("poster.jpg"));
        assertThat(movie.getBackdropPath(), is("backdrop.jpg"));
        assertThat(movie.getVoteAverage(), is(6.66));
        assertThat(movie.getReleaseDate().getYear(), is(2017));
        assertThat(movie.getReleaseDate().getMonthOfYear(), is(11));
        assertThat(movie.getReleaseDate().getDayOfMonth(), is(22));
    }

    private static <T> T getFirstValue(TestObserver<T> testObserver) {
        return testObserver.values().get(0);
    }
}
