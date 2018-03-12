package pl.karollisiewicz.movie.app.animation;


import android.support.annotation.Nullable;

import io.reactivex.functions.Function;
import pl.karollisiewicz.movie.domain.Movie;

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
