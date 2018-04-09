package pl.karollisiewicz.movie.app.data.source.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import pl.karollisiewicz.movie.app.data.source.web.Movie;

/**
 * Adapter that communicates with this shitty content provider's api to access the database and provides
 * clean API to the clients.
 */
public class MovieContentProviderAdapter implements MovieDao {
    private final ContentResolver contentResolver;
    private final MovieContentUriProvider uriProvider;
    private final MovieContentValuesProvider contentValuesProvider;

    @Inject
    MovieContentProviderAdapter(@NonNull final ContentResolver contentResolver,
                                @NonNull final MovieContentUriProvider uriProvider,
                                @NonNull final MovieContentValuesProvider contentValuesProvider) {
        this.contentResolver = contentResolver;
        this.uriProvider = uriProvider;
        this.contentValuesProvider = contentValuesProvider;
    }

    @Override
    public Single<Collection<Movie>> fetchAll() {
        return Single.fromCallable(() -> {
            final Cursor cursor = contentResolver.query(uriProvider.getAll(), null, null, null, null);
            if (cursor == null) return Collections.emptyList();

            return getAllFrom(cursor);
        });
    }

    @NonNull
    private Collection<Movie> getAllFrom(Cursor cursor) {
        final Collection<Movie> movies = new ArrayList<>();
        final MovieCursor movieCursor = new MovieCursor(cursor);
        while (movieCursor.moveToNext()) {
            movies.add(movieCursor.getMovie());
        }
        cursor.close();
        return movies;
    }

    @Override
    public Maybe<Movie> fetchById(long movieId) {
        return Maybe.fromCallable(() -> {
            final Cursor cursor = contentResolver.query(uriProvider.getForId(movieId), null, null, null, null);
            if (cursor == null || cursor.getCount() == 0) return null;

            return getFirstFrom(cursor);
        });
    }

    @NonNull
    private Movie getFirstFrom(@NonNull final Cursor cursor) {
        final MovieCursor movieCursor = new MovieCursor(cursor);
        movieCursor.moveToFirst();
        final Movie movie = movieCursor.getMovie();
        movieCursor.close();
        return movie;
    }

    @Override
    public Single<Movie> save(@NonNull Movie movie) {
        return Single.fromCallable(() -> {
            final ContentValues contentValues = contentValuesProvider.createFrom(movie);
            contentResolver.insert(uriProvider.getAll(), contentValues);
            return movie;
        });
    }

    @Override
    public Completable delete(@NonNull Movie movie) {
        return Completable.fromRunnable(() ->
                contentResolver.delete(uriProvider.getForId(movie.getId()), null, null));
    }
}
