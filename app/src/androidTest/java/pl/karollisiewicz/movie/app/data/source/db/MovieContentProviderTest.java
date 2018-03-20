package pl.karollisiewicz.movie.app.data.source.db;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.TABLE_NAME;

@RunWith(AndroidJUnit4.class)
public class MovieContentProviderTest {
    private SQLiteDatabase database;
    private Context context;

    @Before
    public void beforeEach() {
        context = InstrumentationRegistry.getTargetContext();
        final MovieDatabase movieDatabase = new MovieDatabase(context);
        database = movieDatabase.getWritableDatabase();
    }

    @Test
    public void authorityShouldBeEqualToPackageName() throws Exception {
        final String packageName = context.getPackageName();
        final ComponentName componentName = new ComponentName(packageName, MovieContentProvider.class.getName());
        final PackageManager packageManager = context.getPackageManager();
        final ProviderInfo providerInfo = packageManager.getProviderInfo(componentName, 0);

        assertThat(providerInfo.authority, is(packageName));
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

    @After
    public void afterEach() {
        database.delete(TABLE_NAME, null, null);
    }
}
