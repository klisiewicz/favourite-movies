package pl.karollisiewicz.movie.app.data.source.db;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Favourite movies database contract.
 */
public final class MovieContract {
    static final String AUTHORITY = "pl.karollisiewicz.movie";
    static final Uri BASE_CONTENT_URI = Uri.parse(String.format("%s%s", "content://", AUTHORITY));
    static final String MOVIES_PATH = "movies";
    static final String FAVOURITES_PATH = "favourites";

    private MovieContract() {
        throw new UnsupportedOperationException("No instances of this class should be created.");
    }

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(MOVIES_PATH).appendPath(FAVOURITES_PATH).build();
        public static final String TABLE_NAME = "favourite_movies";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String RELEASE_DATE = "release_date";
    }
}
