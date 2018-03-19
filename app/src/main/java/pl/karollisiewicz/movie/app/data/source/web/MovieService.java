package pl.karollisiewicz.movie.app.data.source.web;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Service for communication with the themoviedb.org endpoint.
 */
public interface MovieService {
    @GET("movie/popular")
    Single<Movies> fetchPopular();

    @GET("movie/top_rated")
    Single<Movies> fetchTopRated();
}