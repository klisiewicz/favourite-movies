package pl.karollisiewicz.movie.domain;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Single;

/**
 * A movie repository.
 */
public interface MovieRepository {
    Single<List<Movie>> fetchBy(@NonNull Criterion criterion);

    enum Criterion {
        POPULARITY, RATHING
    }
}
