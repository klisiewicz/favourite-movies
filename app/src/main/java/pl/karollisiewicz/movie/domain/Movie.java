package pl.karollisiewicz.movie.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.io.Serializable;

public final class Movie implements Serializable {
    private final String title;
    private final String posterUrl;
    private final String backdropUrl;
    private final String overview;
    private final double rating;
    private final LocalDate releaseDate;

    private Movie(@NonNull final Builder builder) {
        title = builder.title;
        posterUrl = builder.posterUrl;
        backdropUrl = builder.backdropUrl;
        overview = builder.overview;
        rating = builder.rating;
        releaseDate = builder.releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    @Nullable
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public static final class Builder {
        private String title;
        private String posterUrl;
        private String backdropUrl;
        private String overview;
        private double rating;
        private LocalDate releaseDate;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
            return this;
        }

        public Builder setBackdropUrl(String backdropUrl) {
            this.backdropUrl = backdropUrl;
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

        public Builder setReleaseDate(LocalDate releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Movie build() {
            return new Movie(this);
        }
    }
}
