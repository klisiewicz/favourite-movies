package pl.karollisiewicz.cinema.app.data.source.web;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Service for communication with the themoviedb.org movie endpoint.
 */
public interface MovieWebService {

    @GET("movie/popular")
    Single<Movies> fetchPopular();

    @GET("movie/top_rated")
    Single<Movies> fetchTopRated();

    @GET("movie/{movie_id}")
    Single<Movie> fetchById(@Path("movie_id") String movieId);
}
