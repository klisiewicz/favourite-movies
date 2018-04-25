package pl.karollisiewicz.cinema.app.data.source.db;


import android.support.annotation.NonNull;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import pl.karollisiewicz.cinema.app.data.source.web.Movie;

public interface MovieDao {
    Single<Collection<Movie>> fetchAll();

    Single<Collection<Movie>> fetchFavourites();

    Maybe<Movie> fetchById(long movieId);

    Single<Movie> save(@NonNull Movie movie);

    Completable delete(@NonNull Movie movie);
}
