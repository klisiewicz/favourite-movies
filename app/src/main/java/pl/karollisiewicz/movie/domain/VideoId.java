package pl.karollisiewicz.movie.domain;

import android.support.annotation.NonNull;

import java.io.Serializable;

public final class VideoId implements Serializable {
    private static final long serialVersionUID = -299846476065947085L;
    private final String value;

    private VideoId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final VideoId videoId = (VideoId) o;
        return value != null ? value.equals(videoId.value) : videoId.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public static VideoId of(@NonNull final String value) {
        return new VideoId(value);
    }
}
