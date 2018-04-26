package pl.karollisiewicz.cinema.domain.movie;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.Collection;

import pl.karollisiewicz.cinema.domain.movie.video.Video;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableCollection;

/**
 * Movie representation.
 */
public final class Movie implements Serializable {
    private static final long serialVersionUID = 8414027612683642476L;
    private final MovieId id;
    private final String title;
    private final String posterUrl;
    private final String backdropUrl;
    private final String overview;
    private final double rating;
    private final LocalDate releaseDate;
    private final Collection<Video> videos;
    private boolean isFavourite;

    private Movie(@NonNull final Builder builder) {
        id = MovieId.of(builder.id);
        title = builder.title;
        posterUrl = builder.posterUrl;
        backdropUrl = builder.backdropUrl;
        overview = builder.overview;
        rating = builder.rating;
        releaseDate = builder.releaseDate;
        videos = builder.videos != null ? unmodifiableCollection(builder.videos) : emptyList();
        isFavourite = builder.isFavourite;
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

    public Collection<Video> getVideos() {
        return videos;
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
        private final long id;
        private String title;
        private String posterUrl;
        private String backdropUrl;
        private String overview;
        private double rating;
        private LocalDate releaseDate;
        private Collection<Video> videos;
        private boolean isFavourite;

        public Builder() {
            this(-1);
        }

        public Builder(long id) {
            this.id = id;
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

        public Builder setVideos(Collection<Video> videos) {
            this.videos = videos;
            return this;
        }

        public Movie build() {
            return new Movie(this);
        }
    }
}
