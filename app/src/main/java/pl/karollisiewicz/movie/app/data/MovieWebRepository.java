package pl.karollisiewicz.movie.app.data;

import android.support.annotation.NonNull;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import pl.karollisiewicz.log.Logger;
import pl.karollisiewicz.movie.app.data.source.db.MovieDao;
import pl.karollisiewicz.movie.app.data.source.web.MovieService;
import pl.karollisiewicz.movie.app.data.source.web.Movies;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository;
import pl.karollisiewicz.movie.domain.exception.AuthorizationException;
import pl.karollisiewicz.movie.domain.exception.CommunicationException;
import pl.karollisiewicz.react.Schedulers;
import retrofit2.HttpException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.FAVOURITE;
import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.POPULARITY;
import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.RATING;

/**
 * Repository that utilizes {@link MovieService} web service to fetch movies.
 */
public final class MovieWebRepository implements MovieRepository {

    private static final int CODE_UNAUTHORIZED = 401;

    private final MovieImageProvider movieImageProvider;
    private final MovieService movieService;
    private final MovieDao movieDao;
    private final Schedulers schedulers;
    private final Logger logger;

    @Inject
    MovieWebRepository(@NonNull final MovieImageProvider movieImageProvider,
                              @NonNull final MovieService movieService,
                              @NonNull final MovieDao movieDao,
                              @NonNull final Schedulers schedulers,
                              @NonNull final Logger logger) {
        this.movieImageProvider = movieImageProvider;
        this.movieService = movieService;
        this.movieDao = movieDao;
        this.schedulers = schedulers;
        this.logger = logger;
    }

    @Override
    public Single<List<Movie>> fetchBy(@NonNull Criterion criterion) {
        return getMoviesSingle(criterion)
                .timeout(3, SECONDS)
                .toObservable()
                .map(Movies::getMovies)
                .flatMapIterable(list -> list)
                .map(this::mapMovie)
                .toList()
                .doOnError(this::logError)
                .onErrorResumeNext(this::mapError)
                .subscribeOn(schedulers.getSubscriber())
                .observeOn(schedulers.getObserver());
    }

    private Single<Movies> getMoviesSingle(@NonNull final Criterion criterion) {
        if (POPULARITY == criterion) return movieService.fetchPopular();
        else if (RATING == criterion) return movieService.fetchTopRated();
        else if (FAVOURITE == criterion) return movieDao.fetchAll().map(movies -> new Movies(new ArrayList<>(movies)));
        else return Single.never();
    }

    private void logError(Throwable throwable) {
        logger.error(MovieWebRepository.class, throwable);
    }

    @NonNull
    private Movie mapMovie(pl.karollisiewicz.movie.app.data.source.web.Movie movie) {
        return new Movie.Builder(movie.getId())
                .setTitle(movie.getTitle())
                .setOverview(movie.getOverview())
                .setRating(movie.getVoteAverage())
                .setReleaseDate(movie.getReleaseDate())
                .setBackdropUrl(movieImageProvider.getBackdropUrl(movie.getBackdropPath()))
                .setPosterUrl(movieImageProvider.getPosterUrl(movie.getPosterPath()))
                .build();
    }

    private SingleSource<? extends List<Movie>> mapError(Throwable throwable) {
        if (throwable instanceof UnknownHostException || throwable instanceof TimeoutException)
            return Single.error(new CommunicationException(throwable));
        else if (throwable instanceof HttpException && ((HttpException) throwable).code() == CODE_UNAUTHORIZED)
            return Single.error(new AuthorizationException(throwable));
        else return Single.error(throwable);
    }
}
