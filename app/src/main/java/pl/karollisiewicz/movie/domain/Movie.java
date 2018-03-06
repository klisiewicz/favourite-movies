package pl.karollisiewicz.movie.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;

public final class Movie implements Serializable {
    private final String title;
    private final String imageUrl;
    private final String overview;
    private final double rating;
    private final Date releaseDate;

    private Movie(@NonNull final Builder builder) {
        title = builder.title;
        imageUrl = builder.imageUrl;
        overview = builder.overview;
        rating = builder.rating;
        releaseDate = builder.releaseDate != null ? new Date(builder.releaseDate.getTime()) : null;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    @Nullable
    public Date getReleaseDate() {
        return releaseDate != null ? new Date(releaseDate.getTime()) : null;
    }

    public static final class Builder {
        private String title;
        private String imageUrl;
        private String overview;
        private double rating;
        private Date releaseDate;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setOverview(String overview) {
            this.overview = overview;
            return this;
        }

        public Builder setRating(double rating) {
            this.rating = rating;
            return this;
        }

        public Builder setReleaseDate(Date releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Movie build() {
            return new Movie(this);
        }
    }
}
