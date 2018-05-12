package pl.karollisiewicz.cinema.domain.movie;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.Collection;

import pl.karollisiewicz.cinema.domain.movie.review.Review;
import pl.karollisiewicz.cinema.domain.movie.video.Video;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableCollection;

/**
 * MovieDetails representation.
 */
public final class MovieDetails implements Serializable {
    private static final long serialVersionUID = 8414027612683642476L;
    private final Movie movie;
    private final String overview;
    private final Collection<Video> videos;
    private final Collection<Review> reviews;
    private boolean isFavourite;

    private MovieDetails(@NonNull final Builder builder) {
        movie = new Movie.Builder(builder.id)
                .setTitle(builder.title)
                .setBackdropUrl(builder.backdropUrl)
                .setPosterUrl(builder.posterUrl)
                .setRating(builder.rating)
                .setReleaseDate(builder.releaseDate)
                .build();
        overview = builder.overview;
        videos = builder.videos != null ? unmodifiableCollection(builder.videos) : emptyList();
        reviews = builder.reviews != null ? unmodifiableCollection(builder.reviews) : emptyList();
        isFavourite = builder.isFavourite;
    }

    public MovieId getId() {
        return movie.getId();
    }

    public String getTitle() {
        return movie.getTitle();
    }

    public String getPosterUrl() {
        return movie.getPosterUrl();
    }

    public String getBackdropUrl() {
        return movie.getBackdropUrl();
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return movie.getRating();
    }

    @Nullable
    public LocalDate getReleaseDate() {
        return movie.getReleaseDate();
    }

    public Collection<Video> getVideos() {
        return videos;
    }

    public Collection<Review> getReviews() {
        return reviews;
    }

    public void favourite() {
        isFavourite = true;
    }

    public void unfavourite() {
        isFavourite = false;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public static final class Builder {
        private final String id;
        private String title;
        private String posterUrl;
        private String backdropUrl;
        private String overview;
        private double rating;
        private LocalDate releaseDate;
        private Collection<Video> videos;
        private Collection<Review> reviews;
        private boolean isFavourite;

        private Builder(String id) {
            this.id = id;
        }

        private Builder(long id) {
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

        public Builder setFavourite(boolean isFavourite) {
            this.isFavourite = isFavourite;
            return this;
        }

        public Builder setReviews(Collection<Review> reviews) {
            this.reviews = reviews;
            return this;
        }

        public Builder setVideos(Collection<Video> videos) {
            this.videos = videos;
            return this;
        }

        public MovieDetails build() {
            return new MovieDetails(this);
        }

        public static Builder withId(long id) {
            return new Builder(id);
        }

        public static Builder from(@NonNull final Movie movie) {
            return new Builder(movie.getId().getValue())
                    .setBackdropUrl(movie.getBackdropUrl())
                    .setPosterUrl(movie.getPosterUrl())
                    .setTitle(movie.getTitle())
                    .setReleaseDate(movie.getReleaseDate())
                    .setRating(movie.getRating());
        }

        public static Builder from(@NonNull final MovieDetails movie) {
            return from(movie.movie)
                    .setFavourite(movie.isFavourite)
                    .setOverview(movie.getOverview())
                    .setVideos(movie.getVideos());

        }
    }
}
