package pl.karollisiewicz.movie.app.data.source.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.content.UriMatcher.NO_MATCH;
import static android.provider.BaseColumns._ID;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.AUTHORITY;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MOVIES_PATH;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.TABLE_NAME;

public class MovieContentProvider extends ContentProvider {
    static final int MOVIES = 100;
    static final int MOVIE_WITH_ID = 101;
    static final UriMatcher URI_MATCHER = new UriMatcher(NO_MATCH);
    private static final String MOVIES_CONTENT_TYPE = "vnd.android.cursor.dir";
    private static final String MOVIE_CONTENT_TYPE = "vnd.android.cursor.item";

    static {
        URI_MATCHER.addURI(AUTHORITY, MOVIES_PATH, MOVIES);
        URI_MATCHER.addURI(AUTHORITY, MOVIES_PATH + "/#", MOVIE_WITH_ID);
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
        int match = getMatch(uri);

        if (match == MOVIES) return fetchAll(uri, projection, selection, selectionArgs, sortOrder);
        else if (match == MOVIE_WITH_ID)
            return fetchById(uri, projection, selection, selectionArgs, sortOrder);
        else throw new IllegalArgumentException("Unknown uri: " + uri);
    }

    private Cursor fetchAll(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                            @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase database = movieDatabase.getReadableDatabase();

        final Cursor cursor = database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor fetchById(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                             @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase database = movieDatabase.getReadableDatabase();
        final SQLiteQueryBuilder queryBuilder = createQueryBuilder(getIdFrom(uri));

        final Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private static String getIdFrom(@NonNull Uri uri) {
        return uri.getLastPathSegment();
    }

    @NonNull
    private SQLiteQueryBuilder createQueryBuilder(final String movieId) {
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);
        queryBuilder.appendWhere(_ID + "=" + movieId);
        return queryBuilder;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = getMatch(uri);

        if (match == MOVIES) return insert(values);
        else throw new IllegalArgumentException("Unknown uri: " + uri);
    }

    private Uri insert(@Nullable ContentValues values) {
        final SQLiteDatabase database = movieDatabase.getWritableDatabase();

        long id = database.insert(TABLE_NAME, null, values);
        if (id > 0) return ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
        else throw new SQLException("Failed to insert row");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = getMatch(uri);

        if (match == MOVIE_WITH_ID) return deleteById(getIdFrom(uri));
        else throw new IllegalArgumentException("Unknown uri: " + uri);
    }

    private int deleteById(String id) {
        final SQLiteDatabase database = movieDatabase.getWritableDatabase();
        return database.delete(TABLE_NAME, _ID + "=?", new String[]{id});
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = getMatch(uri);

        if (match == MOVIES)
            return String.format("%s/%s/%s", MOVIES_CONTENT_TYPE, AUTHORITY, MOVIES_PATH);
        else if (match == MOVIE_WITH_ID)
            return String.format("%s/%s/%s", MOVIE_CONTENT_TYPE, AUTHORITY, MOVIES_PATH);
        else throw new IllegalArgumentException("Unknown uri: " + uri);
    }

    private static int getMatch(@NonNull Uri uri) {
        return URI_MATCHER.match(uri);
    }
}
