package pl.karollisiewicz.cinema.domain.movie;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.io.Serializable;

public final class Movie implements Serializable {
    private static final long serialVersionUID = 3350769575740921839L;

    private final MovieId id;
    private final String title;
    private final String posterUrl;
    private final String backdropUrl;
    private final double rating;
    private final LocalDate releaseDate;

    private Movie(@NonNull final Builder builder) {
        id = MovieId.of(builder.id);
        title = builder.title;
        posterUrl = builder.posterUrl;
        rating = builder.rating;
        releaseDate = builder.releaseDate;
        backdropUrl = builder.backdropUrl;
    }

    public MovieId getId() {
        return id;
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

    public double getRating() {
        return rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public static class Builder {
        private String id;
        private String title;
        private String posterUrl;
        private String backdropUrl;
        private double rating;
        private LocalDate releaseDate;

        public Builder() {
            this("");
        }

        public Builder (String id) {
            this.id = id;
        }

        public Builder (long id) {
            this(String.valueOf(id));
        }

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
