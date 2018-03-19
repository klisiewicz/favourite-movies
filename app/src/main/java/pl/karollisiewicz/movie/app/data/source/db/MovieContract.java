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

    static final class MovieEntry implements BaseColumns {
        static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(MOVIES_PATH).appendPath(FAVOURITES_PATH).build();
        static final String TABLE_NAME = "favourite_movies";

        enum Column {
            ID(MovieEntry._ID, "INTEGER PRIMARY KEY"),
            TITLE("title", "TEXT", "NOT NULL"),
            POSTER_PATH("poster_path", "TEXT"),
            RELEASE_DATE("release_date", "TEXT"),
            VOTE_AVERAGE("vote_average", "REAL");

            private final String name;
            private final String type;
            private final String properties;

            Column(String name, String type) {
                this(name, type, "");
            }

            Column(String name, String type, String properties) {
                this.name = name;
                this.type = type;
                this.properties = properties;
            }


            @Override
            public String toString() {
                return String.format("%s %s %s", name, type, properties);
            }
        }
    }
}
