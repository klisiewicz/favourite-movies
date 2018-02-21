package pl.karollisiewicz.movie.app.source;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository;

import static pl.karollisiewicz.movie.BuildConfig.IMAGE_URL;

/**
 * Repository that utilizes {@link MovieService} web service to fetch movies.
 */
public final class MovieWebRepository implements MovieRepository {
    private final MovieService movieService;

    @Inject
    public MovieWebRepository(@NonNull final MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public Single<List<Movie>> fetchBy(@NonNull Criterion criterion) {
        return movieService.fetchPopular()
                .toObservable()
                .map(Movies::getMovies)
                .flatMapIterable(list -> list)
                .map(it -> new Movie.Builder()
                        .setTitle(it.getTitle())
                        .setOverview(it.getOverview())
                        .setRating(it.getVoteAverage())
                        .setReleaseDate(null)
                        .setImageUrl(String.format("%s%s", IMAGE_URL, it.getPosterPath()))
                        .build()
                )
                .toList();
    }
}
