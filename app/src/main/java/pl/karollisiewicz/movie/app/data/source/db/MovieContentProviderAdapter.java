package pl.karollisiewicz.movie.app.data.source.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import pl.karollisiewicz.movie.app.data.source.web.Movie;

/**
 * Adapter that communicates with content provider to access the database.
 */
public final class MovieContentProviderAdapter implements MovieDao {
    private final ContentResolver contentResolver;

    @Inject
    MovieContentProviderAdapter(@NonNull final Context context) {
        contentResolver = context.getContentResolver();
    }

    @Override
    public Single<Collection<Movie>> fetchAll() {
        return Single.fromCallable(() -> {
            final Collection<Movie> movies = new ArrayList<>();
            final Cursor cursor = contentResolver.query(MovieContentProvider.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                final MovieCursor movieCursor = new MovieCursor(cursor);
                while (cursor.moveToNext()) {
                    final Movie m = new Movie();
                    m.setId(movieCursor.getId());
                    m.setTitle(movieCursor.getTitle());
                    m.setOverview(movieCursor.getOverview());
                    m.setPosterPath(movieCursor.getPosterPath());
                    m.setBackdropPath(movieCursor.getBackdropPath());
                    m.setVoteAverage(movieCursor.getVoteAverage());
                    m.setReleaseDate(LocalDate.fromDateFields(new Date(movieCursor.getReleaseTimestamp())));
                    movies.add(m);
                }
                cursor.close();
            }
            return movies;
        });
    }

    @Override
    public Single<Movie> save(Movie movie) {
        return Single.just(movie);
    }

    @Override
    public Completable delete(Movie movie) {
        return Completable.complete();
    }
}
