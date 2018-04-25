package pl.karollisiewicz.cinema.app.animation;


import android.support.annotation.Nullable;

import io.reactivex.functions.Function;
import pl.karollisiewicz.cinema.domain.Movie;

/**
 * This class is capable of providing a common transition name.
 */
public final class TransitionNameSupplier implements Function<Movie, String> {
    private static final TransitionNameSupplier INSTANCE = new TransitionNameSupplier();

    private TransitionNameSupplier() {
    }

    public static TransitionNameSupplier getInstance() {
        return INSTANCE;
    }

    @Override
    public String apply(@Nullable final Movie movie) {
        return movie != null ? movie.getTitle() : "";
    }
}
