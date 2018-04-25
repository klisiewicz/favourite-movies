package pl.karollisiewicz.cinema.app.data.source.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import pl.karollisiewicz.cinema.app.data.source.web.Movie;

import static pl.karollisiewicz.cinema.app.data.source.db.MovieContract.MovieEntry.Column.FAVOURITE;
import static pl.karollisiewicz.cinema.app.data.source.db.MovieContract.MovieEntry.Column.getName;

/**
 * Adapter that communicates with this shitty content provider's api to access the database and provides
 * clean API to the clients.
 */
public final class MovieContentProviderAdapter implements MovieDao {
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
        return Single.fromCallable(() -> fetch(null, null));
    }

    @NonNull
    private Collection<Movie> fetch(@Nullable String selection, @Nullable String[] selectionArgs) {
        final Cursor cursor = contentResolver.query(uriProvider.getAll(), null, selection, selectionArgs, null);
        if (cursor == null) return Collections.emptyList();

        return getAllFrom(cursor);
    }

    @Override
    public Single<Collection<Movie>> fetchFavourites() {
        return Single.fromCallable(() -> fetch(getName(FAVOURITE) + "=?", new String[]{"1"}));
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
            final Cursor cursor = queryById(movieId);
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
            final Cursor cursor = queryById(movie.getId());
            return updateOrInsert(movie, cursor);
        });
    }

    private Cursor queryById(long id) {
        return contentResolver.query(uriProvider.getForId(id), null, null, null, null);
    }

    private Movie updateOrInsert(@NonNull Movie movie, Cursor cursor) {
        final ContentValues contentValues = contentValuesProvider.createFrom(movie);

        if (cursor == null || cursor.getCount() == 0) {
            contentResolver.insert(uriProvider.getAll(), contentValues);
        }
        else {
            contentResolver.update(uriProvider.getForId(movie.getId()), contentValues, null, null);
            cursor.close();
        }

        return movie;
    }

    @Override
    public Completable delete(@NonNull Movie movie) {
        return Completable.fromRunnable(() ->
                contentResolver.delete(uriProvider.getForId(movie.getId()), null, null));
    }
}
