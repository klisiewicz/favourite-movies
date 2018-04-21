package pl.karollisiewicz.movie.app.data.source.db;

import android.database.Cursor;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.Date;

import pl.karollisiewicz.movie.app.data.source.web.Movie;

import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.BACKDROP_PATH;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.FAVOURITE;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.ID;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.OVERVIEW;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.POSTER_PATH;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.RELEASE_DATE;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.TITLE;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.VOTE_AVERAGE;

/**
 * Cursor which provides methods allowing direct access to the database column values.
 */
final class MovieCursor {
    private final Cursor cursor;

    MovieCursor(@NonNull final Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    Movie getMovie() {
        final Movie movie = new Movie();
        movie.setId(getId());
        movie.setTitle(getTitle());
        movie.setOverview(getOverview());
        movie.setPosterPath(getPosterPath());
        movie.setBackdropPath(getBackdropPath());
        movie.setVoteAverage(getVoteAverage());
        movie.setReleaseDate(LocalDate.fromDateFields(new Date(getReleaseTimestamp())));
        movie.setFavourite(isFavourite());
        return movie;
    }

    private long getId() {
        return cursor.getInt(cursor.getColumnIndex(ID.getName()));
    }

    private String getTitle() {
        return cursor.getString(cursor.getColumnIndex(TITLE.getName()));
    }

    private String getOverview() {
        return cursor.getString(cursor.getColumnIndex(OVERVIEW.getName()));
    }

    private String getPosterPath() {
        return cursor.getString(cursor.getColumnIndex(POSTER_PATH.getName()));
    }

    private String getBackdropPath() {
        return cursor.getString(cursor.getColumnIndex(BACKDROP_PATH.getName()));
    }

    private double getVoteAverage() {
        return cursor.getDouble(cursor.getColumnIndex(VOTE_AVERAGE.getName()));
    }

    private long getReleaseTimestamp() {
        return cursor.getLong(cursor.getColumnIndex(RELEASE_DATE.getName()));
    }

    private boolean isFavourite() {
        return cursor.getInt(cursor.getColumnIndex(FAVOURITE.getName())) == 1;
    }

    public boolean moveToFirst() {
        return cursor.moveToFirst();
    }

    public boolean moveToNext() {
        return cursor.moveToNext();
    }

    public int getCount() {
        return cursor.getCount();
    }

    public void close() {
        cursor.close();
    }
}
