package pl.karollisiewicz.movie.domain;

import android.support.annotation.NonNull;

import java.io.Serializable;

public final class Video implements Serializable {
    private static final long serialVersionUID = -608520751973739291L;
    private final VideoId id;
    private final String key;
    private final String name;
    private final String site;
    private final Quality quality;
    private final Type type;

    private Video(@NonNull final Builder builder) {
        id = VideoId.of(builder.id);
        key = builder.key;
        name = builder.name;
        site = builder.site;
        quality = builder.quality;
        type = builder.type;
    }

    public VideoId getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public Quality getQuality() {
        return quality;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        TRAILER, TEASER, CLIP, FEATURETTE
    }

    public enum Quality {
        SD, HALF_HD, FULL_HD, ULTRA_HD
    }

    public static final class Builder {
        private String id;
        private String key;
        private String name;
        private String site;
        private Quality quality;
        private Type type;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSite(String site) {
            this.site = site;
            return this;
        }

        public Builder setQuality(Quality quality) {
            this.quality = quality;
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
