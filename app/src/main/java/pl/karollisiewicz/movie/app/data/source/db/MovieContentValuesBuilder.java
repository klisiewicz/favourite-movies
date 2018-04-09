package pl.karollisiewicz.movie.app.data.source.db;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import pl.karollisiewicz.movie.app.data.source.web.Movie;

final class MovieContentValuesBuilder {
    private final Movie movie;

    private MovieContentValuesBuilder(Movie movie) {
        this.movie = movie;
    }

    @NonNull
    ContentValues build() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.Column.ID.getName(), movie.getId());
        contentValues.put(MovieContract.MovieEntry.Column.TITLE.getName(), movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.Column.OVERVIEW.getName(), movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.Column.POSTER_PATH.getName(), movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.Column.BACKDROP_PATH.getName(), movie.getBackdropPath());
        contentValues.put(MovieContract.MovieEntry.Column.VOTE_AVERAGE.getName(), movie.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.Column.RELEASE_DATE.getName(), movie.getReleaseDate().toDate().getTime());
        return contentValues;
    }

    static MovieContentValuesBuilder withMovie(final @NonNull Movie movie) {
        return new MovieContentValuesBuilder(movie);
    }
}
