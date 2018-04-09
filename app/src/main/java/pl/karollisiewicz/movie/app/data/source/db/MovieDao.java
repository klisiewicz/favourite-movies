package pl.karollisiewicz.movie.app.data.source.db;


import android.support.annotation.NonNull;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import pl.karollisiewicz.movie.app.data.source.web.Movie;

public interface MovieDao {
    Single<Collection<Movie>> fetchAll();

    Maybe<Movie> fetchById(long movieId);

    Single<Movie> save(@NonNull Movie movie);

    Completable delete(@NonNull Movie movie);
}
