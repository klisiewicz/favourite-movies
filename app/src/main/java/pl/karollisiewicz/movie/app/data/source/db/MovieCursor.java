package pl.karollisiewicz.movie.app.data.source.db;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.support.annotation.NonNull;

import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.BACKDROP_PATH;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.ID;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.OVERVIEW;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.POSTER_PATH;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.RELEASE_DATE;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.TITLE;
import static pl.karollisiewicz.movie.app.data.source.db.MovieContract.MovieEntry.Column.VOTE_AVERAGE;

/**
 * Cursor which provides methods allowing direct access to the database column values.
 */
final class MovieCursor extends CursorWrapper {

    MovieCursor(@NonNull final Cursor cursor) {
        super(cursor);
    }

    long getId() {
        return getWrappedCursor().getInt(getWrappedCursor().getColumnIndex(ID.getName()));
    }

    String getTitle() {
        return getWrappedCursor().getString(getWrappedCursor().getColumnIndex(TITLE.getName()));
    }

    String getOverview() {
        return getWrappedCursor().getString(getWrappedCursor().getColumnIndex(OVERVIEW.getName()));
    }

    String getPosterPath() {
        return getWrappedCursor().getString(getWrappedCursor().getColumnIndex(POSTER_PATH.getName()));
    }

    String getBackdropPath() {
        return getWrappedCursor().getString(getWrappedCursor().getColumnIndex(BACKDROP_PATH.getName()));
    }

    double getVoteAverage() {
        return getWrappedCursor().getDouble(getWrappedCursor().getColumnIndex(VOTE_AVERAGE.getName()));
    }

    int getReleaseTimestamp() {
        return getWrappedCursor().getInt(getWrappedCursor().getColumnIndex(RELEASE_DATE.getName()));
    }
}
