package pl.karollisiewicz.movie.app.source;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import pl.karollisiewicz.movie.app.react.Schedulers;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository;

import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.POPULARITY;
import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.RATING;

/**
 * Repository that utilizes {@link MovieService} web service to fetch movies.
 */
public final class MovieWebRepository implements MovieRepository {

    private final MovieImageProvider movieImageProvider;
    private final MovieService movieService;
    private final Schedulers schedulers;

    @Inject
    MovieWebRepository(@NonNull final MovieImageProvider movieImageProvider,
                       @NonNull final MovieService movieService,
                       @NonNull final Schedulers schedulers) {
        this.movieImageProvider = movieImageProvider;
        this.movieService = movieService;
        this.schedulers = schedulers;
    }

    @Override
    public Single<List<Movie>> fetchBy(@NonNull Criterion criterion) {
        return getMoviesSingle(criterion)
                .toObservable()
                .map(Movies::getMovies)
                .flatMapIterable(list -> list)
                .map(this::mapMovie)
                .toList()
                .subscribeOn(schedulers.getSubscriber())
                .observeOn(schedulers.getObserver());
    }

    @NonNull
    private Movie mapMovie(pl.karollisiewicz.movie.app.source.Movie movie) {
        return new Movie.Builder()
                .setTitle(movie.getTitle())
                .setOverview(movie.getOverview())
                .setRating(movie.getVoteAverage())
                .setReleaseDate(movie.getReleaseDate())
                .setBackdropUrl(movieImageProvider.getBackdropUrl(movie.getBackdropPath()))
                .setPosterUrl(movieImageProvider.getPosterUrl(movie.getPosterPath()))
                .build();
    }

    private Single<Movies> getMoviesSingle(@NonNull final Criterion criterion) {
        if (POPULARITY == criterion) return movieService.fetchPopular();
        else if (RATING == criterion) return movieService.fetchTopRated();
        else return Single.never();
    }
}
