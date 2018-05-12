package pl.karollisiewicz.cinema.app.data.source.web.review;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReviewService {
    @GET("movie/{movie_id}/reviews")
    Single<Reviews> fetchBy(@Path("movie_id") String movieId);
}
