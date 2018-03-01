package pl.karollisiewicz.movie.app.source;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import pl.karollisiewicz.movie.app.react.Schedulers;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository;

/**
 * Repository that utilizes {@link MovieService} web service to fetch movies.
 */
public final class MovieWebRepository implements MovieRepository {
    private final String imageUrl;
    private final MovieService movieService;
    private final Schedulers schedulers;

    @Inject
    MovieWebRepository(@NonNull @Named("image-url") String imageUrl,
                       @NonNull final MovieService movieService,
                       @NonNull final Schedulers schedulers) {
        this.imageUrl = imageUrl;
        this.movieService = movieService;
        this.schedulers = schedulers;
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
                        .setReleaseDate(it.getReleaseDate())
                        .setImageUrl(String.format("%s%s", imageUrl, it.getPosterPath()))
                        .build()
                )
                .toList()
                .subscribeOn(schedulers.getSubscriber())
                .observeOn(schedulers.getObserver());
    }
}
