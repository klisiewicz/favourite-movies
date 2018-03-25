package pl.karollisiewicz.movie.app.data.source.db;

import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
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
    public Flowable<Collection<Movie>> fetchAll() {
        return Flowable.never();
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
