package pl.karollisiewicz.movie.domain;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

/**
 * A movie repository.
 */
public interface MovieRepository {
    LiveData<Iterable<Movie>> fetchBy(@NonNull Criterion criterion);

    enum Criterion {
        POPULARITY, RATHING
    }
}
