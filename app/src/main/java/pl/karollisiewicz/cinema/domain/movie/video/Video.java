package pl.karollisiewicz.cinema.domain.movie.video;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

public final class Video implements Serializable {
    private static final long serialVersionUID = -608520751973739291L;
    private final String url;
    private final String thumbnailUrl;
    private final String name;
    private final Type type;

    private Video(@NonNull final Builder builder) {
        url = builder.url;
        thumbnailUrl = builder.thumbnailUrl;
        name = builder.name;
        type = builder.type;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public Type getType() {
        return type;
    }

    public enum Type {
        TRAILER, TEASER, CLIP, FEATURETTE
    }

    public static final class Builder {
        private String id;
        private String url;
        private String thumbnailUrl;
        private String name;
        private Type type;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Video build() {
            return new Video(this);
        }
    }
}
