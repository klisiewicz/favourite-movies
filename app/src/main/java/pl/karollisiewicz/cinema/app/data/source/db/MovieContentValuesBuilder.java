package pl.karollisiewicz.cinema.app.data.source.db;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import pl.karollisiewicz.cinema.app.data.source.web.Movie;

import static pl.karollisiewicz.cinema.app.data.source.db.MovieContract.MovieEntry.Column.BACKDROP_PATH;
import static pl.karollisiewicz.cinema.app.data.source.db.MovieContract.MovieEntry.Column.FAVOURITE;
import static pl.karollisiewicz.cinema.app.data.source.db.MovieContract.MovieEntry.Column.ID;
import static pl.karollisiewicz.cinema.app.data.source.db.MovieContract.MovieEntry.Column.POSTER_PATH;
import static pl.karollisiewicz.cinema.app.data.source.db.MovieContract.MovieEntry.Column.RELEASE_DATE;
import static pl.karollisiewicz.cinema.app.data.source.db.MovieContract.MovieEntry.Column.TITLE;
import static pl.karollisiewicz.cinema.app.data.source.db.MovieContract.MovieEntry.Column.VOTE_AVERAGE;
import static pl.karollisiewicz.cinema.app.data.source.db.MovieContract.MovieEntry.Column.getName;

final class MovieContentValuesBuilder {
    private final Movie movie;

    private MovieContentValuesBuilder(Movie movie) {
        this.movie = movie;
    }

    @NonNull
    ContentValues build() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(getName(ID), movie.getId());
        contentValues.put(getName(TITLE), movie.getTitle());
        contentValues.put(getName(POSTER_PATH), movie.getPosterPath());
        contentValues.put(getName(BACKDROP_PATH), movie.getBackdropPath());
        contentValues.put(getName(VOTE_AVERAGE), movie.getVoteAverage());
        contentValues.put(getName(RELEASE_DATE), movie.getReleaseDate().toDate().getTime());
        contentValues.put(getName(FAVOURITE), movie.isFavourite() ? 1 : 0);
        return contentValues;
    }

    static MovieContentValuesBuilder withMovie(final @NonNull Movie movie) {
        return new MovieContentValuesBuilder(movie);
    }
}
