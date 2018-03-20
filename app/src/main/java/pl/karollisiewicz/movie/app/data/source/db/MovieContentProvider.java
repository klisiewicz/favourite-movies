package pl.karollisiewicz.movie.app.data.source.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.content.UriMatcher.NO_MATCH;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.AUTHORITY;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.TABLE_NAME;

public class MovieContentProvider extends ContentProvider {
    static final int MOVIES = 100;
    static final int MOVIE_WITH_ID = 101;
    static final UriMatcher URI_MATCHER = new UriMatcher(NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, MovieContract.MOVIES_PATH, MOVIES);
        URI_MATCHER.addURI(AUTHORITY, MovieContract.MOVIES_PATH + "/#", MOVIE_WITH_ID);
    }

    private MovieDatabase movieDatabase;

    @Override
    public boolean onCreate() {
        movieDatabase = new MovieDatabase(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = URI_MATCHER.match(uri);

        if (match == MOVIES) return insert(values);
        else throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    private Uri insert(@Nullable ContentValues values) {
        final SQLiteDatabase database = movieDatabase.getWritableDatabase();

        long id = database.insert(TABLE_NAME, null, values);
        if (id > 0) return ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
        else throw new SQLException("Failed to insert row");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
