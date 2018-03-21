package pl.karollisiewicz.movie.app.data.source.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.String.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.CONTENT_URI;

@RunWith(AndroidJUnit4.class)
public class MovieContentProviderTest extends ProviderTestCase2 {
    private static final int ID = 6;
    private static final int INVALID_ID = Integer.MAX_VALUE;
    private static final String TITLE = "Title";

    private Cursor cursor;

    public MovieContentProviderTest() {
        super(MovieContentProvider.class, MovieContract.AUTHORITY);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
    }

    @Test
    public void shouldMatchMoviesUri() {
        // When: Matching movies code
        int actualMatchCode = MovieContentProvider.URI_MATCHER.match(CONTENT_URI);

        // Then: Code is equal to Movies constant
        assertThat(actualMatchCode, is(MovieContentProvider.MOVIES));
    }

    @Test
    public void shouldMatchAMovieWithIdUri() {
        // Given:
        final Uri movieUri = CONTENT_URI.buildUpon().appendPath("1").build();

        // When: Matching a movie code
        int actualMatchCode = MovieContentProvider.URI_MATCHER.match(movieUri);

        // Then: Code is equal to Movies constant
        assertThat(actualMatchCode, is(MovieContentProvider.MOVIE_WITH_ID));
    }

    @Test
    public void whenMovieContentValuesAreValid_ItIsInserted() {
        final ContentValues validMovie = validMovie();

        final Uri uri = getMockContentResolver().insert(CONTENT_URI, validMovie);

        assertThat(uri, is(not(nullValue())));
    }

    @Test
    public void whenMovieContentValuesAreInvalid_AnExceptionIsThrown() {
        final ContentValues invalidMovie = invalidMovie();

        try {
            final Uri uri = getMockContentResolver().insert(CONTENT_URI, invalidMovie);
            fail("No exception thrown.");
        } catch (Exception e) {
            assertThat(e, is(instanceOf(SQLException.class)));
        }
    }

    @NonNull
    private ContentValues validMovie() {
        return getContentValues(ID, TITLE);
    }

    @NonNull
    private ContentValues invalidMovie() {
        return getContentValues(ID, null);
    }

    @NonNull
    private ContentValues getContentValues(int id, String title) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.Column.ID.getName(), id);
        contentValues.put(MovieContract.MovieEntry.Column.TITLE.getName(), title);
        return contentValues;
    }

    @Test
    public void whenNoRecordsInDatabase_EmptyCursorIsReturned() {
        givenEmptyDatabase();

        whenFetchingAllMovies();

        thenCursorIsEmpty();
    }

    @Test
    public void whenThereAreRecordsInTheDatabase_CursorContainsAllOfThem() {
        givenDatabaseWithMovie();

        whenFetchingAllMovies();

        thenCursorIsNotEmpty();
        thenFirstRecordHasIdAndTitle(ID, TITLE);
    }

    @Test
    public void whenThereIsNoRecordWithGivenId_CursorIsEmpty() {
        givenDatabaseWithMovie(ID, TITLE);

        whenFetchingMovieWithId(INVALID_ID);

        thenCursorIsEmpty();
    }

    @Test
    public void whenThereIARecordWithGivenId_CursorContainsIt() {
        givenDatabaseWithMovie(ID, TITLE);

        whenFetchingMovieWithId(ID);

        thenCursorIsNotEmpty();
        thenFirstRecordHasIdAndTitle(ID, TITLE);
    }

    @Test
    public void whenRemovingRecordExists_ItShouldBeDeleted() {
        givenDatabaseWithMovie(ID, TITLE);

        whenDeletingMovieWithId(ID);

        thenMovieWithIdDoesNotExist(ID);
    }

    @Test
    public void whenRemovingDoesNotRecordExist_ItShouldNotBeDeleted() {
        givenDatabaseWithMovie(ID, TITLE);

        whenDeletingMovieWithId(INVALID_ID);

        thenMovieWithIdExists(ID);
    }

    private void givenEmptyDatabase() {
        // Do nothing - database should be empty
    }

    private void givenDatabaseWithMovie() {
        givenDatabaseWithMovie(ID, TITLE);
    }

    private void givenDatabaseWithMovie(int id, String title) {
        getMockContentResolver().insert(CONTENT_URI, getContentValues(id, title));
    }

    private void whenFetchingAllMovies() {
        cursor = getMockContentResolver().query(CONTENT_URI, null, null, null, null);
    }

    private void whenFetchingMovieWithId(int id) {
        final Uri movieUri = CONTENT_URI.buildUpon().appendPath(valueOf(id)).build();
        cursor = getMockContentResolver().query(movieUri, null, null, null, null);
    }

    private void whenDeletingMovieWithId(int id) {
        final Uri movieUri = CONTENT_URI.buildUpon().appendPath(valueOf(id)).build();
        getMockContentResolver().delete(movieUri, null, null);
    }

    private void thenCursorIsEmpty() {
        assertThat(cursor, is(not(nullValue())));
        assertThat(cursor.getCount(), is(0));
    }

    private void thenCursorIsNotEmpty() {
        assertThat(cursor, is(not(nullValue())));
        assertThat(cursor.getCount(), is(1));
    }

    private void thenFirstRecordHasIdAndTitle(int id, String title) {
        cursor.moveToFirst();
        assertThat(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.Column.ID.getName())), is(id));
        assertThat(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.Column.TITLE.getName())), is(title));
    }

    private void thenMovieWithIdDoesNotExist(int id) {
        whenFetchingMovieWithId(id);
        thenCursorIsEmpty();
    }

    private void thenMovieWithIdExists(int id) {
        whenFetchingMovieWithId(id);
        thenCursorIsNotEmpty();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        if (cursor != null) cursor.close();
    }
}
