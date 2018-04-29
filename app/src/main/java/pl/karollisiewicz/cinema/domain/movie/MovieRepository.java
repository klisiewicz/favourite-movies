package pl.karollisiewicz.cinema.domain.movie;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * A repository for fetching movies.
 */
public interface MovieRepository {
    Single<List<Movie>> fetchBy(@NonNull Criterion criterion);

    Maybe<MovieDetails> fetchBy(@NonNull MovieId movieId);

    Single<MovieDetails> save(@NonNull MovieDetails movie);

    enum Criterion {
        POPULARITY, RATING, FAVOURITE
    }
}
