package pl.karollisiewicz.movie.app.data.source.db;

import android.content.ContentValues;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.ID;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.TITLE;

@RunWith(AndroidJUnit4.class)
public class MovieContentProviderTest extends ProviderTestCase2 {
    private MockContentResolver mockContentResolver;
//    private SQLiteDatabase database;
//    private Context context;

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
        // Given: Movies Uri
        final Uri moviesUri = MovieContract.MovieEntry.CONTENT_URI;

        // When: Matching movies code
        int actualMatchCode = MovieContentProvider.URI_MATCHER.match(moviesUri);

        // Then: Code is equal to Movies constant
        assertThat(actualMatchCode, is(MovieContentProvider.MOVIES));
    }

    @Test
    public void shouldMatchAMovieWithIdUri() {
        // Given:
        final Uri movieUri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath("1").build();

        // When: Matching a movie code
        int actualMatchCode = MovieContentProvider.URI_MATCHER.match(movieUri);

        // Then: Code is equal to Movies constant
        assertThat(actualMatchCode, is(MovieContentProvider.MOVIE_WITH_ID));
    }

    @Test
    public void whenMovieContentValuesAreValid_ItIsInserted() {
        final ContentValues validMovie = validMovie();

        final Uri uri = getMockContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, validMovie);

        assertThat(uri, is(not(nullValue())));
    }

    @Test
    public void whenMovieContentValuesAreInvalid_AnExceptionIsThrown() {
        final ContentValues invalidMovie = invalidMovie();

        try {
            final Uri uri = getMockContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, invalidMovie);
            fail("No exception thrown.");
        } catch (Exception e) {
            assertThat(e, is(instanceOf(SQLException.class)));
        }
    }

    @NonNull
    private ContentValues validMovie() {
        return getContentValues(6, "A Title");
    }

    @NonNull
    private ContentValues invalidMovie() {
        return getContentValues(9, null);
    }

    @NonNull
    private ContentValues getContentValues(int id, String title) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(ID.getName(), id);
        contentValues.put(TITLE.getName(), title);
        return contentValues;
    }
}
