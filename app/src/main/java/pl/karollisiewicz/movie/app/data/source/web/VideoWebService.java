package pl.karollisiewicz.movie.app.data.source.web;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Service for communication with the themoviedb.org video endpoint.
 */
public interface VideoWebService {

    @GET("movie/{movie_id}/videos")
    Single<Videos> fetchBy(@Path("movie_id") long movieId);
}
