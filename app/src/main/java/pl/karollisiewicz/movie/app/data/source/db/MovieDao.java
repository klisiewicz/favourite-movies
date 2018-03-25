package pl.karollisiewicz.movie.app.data.source.db;


import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import pl.karollisiewicz.movie.app.data.source.web.Movie;

public interface MovieDao {
    Flowable<Collection<Movie>> fetchAll();

    Single<Movie> save(Movie movie);

    Completable delete(Movie movie);
}
