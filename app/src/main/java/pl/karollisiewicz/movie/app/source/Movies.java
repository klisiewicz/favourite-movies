package pl.karollisiewicz.movie.app.source;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity that represents a list of Movies in the themoviedb.org service.
 */
public class Movies implements Serializable {
    private static final long serialVersionUID = -7973336155188735327L;

    @SerializedName("results")
    private List<Movie> movies = new ArrayList<>();

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
