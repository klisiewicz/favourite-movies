package pl.karollisiewicz.movie.app.data.source.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.content.UriMatcher.NO_MATCH;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.AUTHORITY;

public class MovieContentProvider extends ContentProvider {
    static final int MOVIES = 100;
    static final int MOVIE_WITH_ID = 101;
    static final UriMatcher URI_MATCHER = new UriMatcher(NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, MovieContract.MOVIES_PATH, MOVIES);
        URI_MATCHER.addURI(AUTHORITY, MovieContract.MOVIES_PATH + "/#", MOVIE_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
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
}
