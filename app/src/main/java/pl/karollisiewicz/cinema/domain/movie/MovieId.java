package pl.karollisiewicz.cinema.domain.movie;

import android.support.annotation.NonNull;

import java.io.Serializable;

public final class MovieId implements Serializable {
    private static final long serialVersionUID = -607433275614570758L;
    private final String value;

    private MovieId(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MovieId of(@NonNull final String value) {
        return new MovieId(value);
    }

    public static MovieId of(final long value) {
        return of(String.valueOf(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MovieId movieId = (MovieId) o;
        return value != null ? value.equals(movieId.value) : movieId.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
