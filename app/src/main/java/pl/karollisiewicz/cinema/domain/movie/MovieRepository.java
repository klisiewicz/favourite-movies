package pl.karollisiewicz.cinema.domain.movie;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Single;

/**
 * A repository for fetching movies.
 */
public interface MovieRepository {
    Single<List<Movie>> fetchBy(@NonNull Criterion criterion);

    Single<Movie> save(@NonNull Movie movie);

    enum Criterion {
        POPULARITY, RATING, FAVOURITE
    }
}
