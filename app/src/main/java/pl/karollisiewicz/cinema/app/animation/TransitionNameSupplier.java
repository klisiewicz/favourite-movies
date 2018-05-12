package pl.karollisiewicz.cinema.app.animation;


import android.support.annotation.Nullable;

import pl.karollisiewicz.cinema.domain.movie.Movie;
import pl.karollisiewicz.cinema.domain.movie.MovieDetails;

/**
 * This class is capable of providing a common transition name.
 */
public final class TransitionNameSupplier {
    private static final TransitionNameSupplier INSTANCE = new TransitionNameSupplier();

    private TransitionNameSupplier() {
    }

    public static TransitionNameSupplier getInstance() {
        return INSTANCE;
    }

    public String apply(@Nullable final Movie movie) {
        return movie != null ? movie.getTitle() : "";
    }

    public String apply(@Nullable final MovieDetails movie) {
        return movie != null ? movie.getTitle() : "";
    }
}
