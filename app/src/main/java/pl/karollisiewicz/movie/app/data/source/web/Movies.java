package pl.karollisiewicz.movie.app.data.source.web;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Entity that represents a list of Movies in the themoviedb.org service.
 */
public final class Movies implements Serializable {
    private static final long serialVersionUID = -7973336155188735327L;

    @SerializedName("results")
    private List<Movie> movies = Collections.emptyList();

    public Movies() {
        // Required for serialization
    }

    public Movies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
