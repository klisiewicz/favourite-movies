package pl.karollisiewicz.cinema.app.data.source.web;

import android.support.annotation.NonNull;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import pl.karollisiewicz.cinema.app.data.source.db.MovieDao;
import pl.karollisiewicz.cinema.domain.exception.AuthorizationException;
import pl.karollisiewicz.cinema.domain.exception.CommunicationException;
import pl.karollisiewicz.cinema.domain.movie.Movie;
import pl.karollisiewicz.cinema.domain.movie.MovieDetails;
import pl.karollisiewicz.cinema.domain.movie.MovieId;
import pl.karollisiewicz.cinema.domain.movie.MovieRepository;
import pl.karollisiewicz.cinema.domain.movie.video.Video;
import pl.karollisiewicz.common.log.Logger;
import pl.karollisiewicz.common.react.Schedulers;
import retrofit2.HttpException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static pl.karollisiewicz.common.react.Transformers.applySchedulersToMaybe;
import static pl.karollisiewicz.common.react.Transformers.applySchedulersToSingle;

/**
 * Repository that utilizes {@link MovieWebService} web service to fetch movies.
 */
public final class MovieWebRepository implements MovieRepository {
    private static final int CODE_UNAUTHORIZED = 401;

    private final MovieService movieService;
    private final MovieDao movieDao;
    private final VideoService videoService;
    private final Schedulers schedulers;
    private final Logger logger;

    @Inject
    public MovieWebRepository(@NonNull final MovieService movieService,
                              @NonNull final MovieDao movieDao,
                              @NonNull final VideoService videoService,
                              @NonNull final Schedulers schedulers,
                              @NonNull final Logger logger) {
        this.movieService = movieService;
        this.movieDao = movieDao;
        this.videoService = videoService;
        this.schedulers = schedulers;
        this.logger = logger;
    }

    @Override
    public Single<List<Movie>> fetchBy(@NonNull Criterion criterion) {
        return getMoviesMatchingCriterion(criterion)
                .toObservable()
                .map(Movies::getMovies)
                .flatMapIterable(list -> list)
                .map(MovieMapper::toMovie)
                .toList()
                .timeout(5, SECONDS)
                .doOnError(this::logError)
                .onErrorResumeNext(this::mapError)
                .compose(applySchedulersToSingle(schedulers));
    }

    private Single<Movies> getMoviesMatchingCriterion(@NonNull final Criterion criterion) {
        if (Criterion.POPULARITY == criterion)
            return movieService.fetchPopular();
        else if (Criterion.RATING == criterion)
            return movieService.fetchTopRated();
        else if (Criterion.FAVOURITE == criterion)
            return movieDao.fetchFavourites().map(movies -> new Movies(new ArrayList<>(movies)));
        else return Single.never();
    }

    private void logError(Throwable throwable) {
        logger.error(MovieWebRepository.class, throwable);
    }

    private SingleSource<? extends List<Movie>> mapError(Throwable throwable) {
        if (throwable instanceof UnknownHostException || throwable instanceof TimeoutException)
            return Single.error(new CommunicationException(throwable));
        else if (throwable instanceof HttpException && ((HttpException) throwable).code() == CODE_UNAUTHORIZED)
            return Single.error(new AuthorizationException(throwable));
        else return Single.error(throwable);
    }

    @Override
    public Maybe<MovieDetails> fetchBy(@NonNull MovieId movieId) {
        final long id = Long.parseLong(movieId.getValue());
        final Single<MovieDetails> movieObservable = movieService.fetchById(id)
                .map(MovieMapper::toMovieDetails);

        final Single<MovieDetails> favouriteObservable = movieDao.fetchById(id)
                .map(MovieMapper::toMovieDetails)
                .switchIfEmpty(Single.just(MovieDetails.Builder.withId(-1L).build()));

        final Single<List<Video>> videosObservable = videoService
                .fetchBy(id)
                .toObservable()
                .map(Videos::getVideos)
                .flatMapIterable(list -> list)
                .map(VideoMapper::toDomain)
                .toList();

        return Single.zip(
                movieObservable, favouriteObservable, videosObservable, (movie, favourite, videos) -> {
                    if (favourite != null && favourite.isFavourite()) movie.favourite();
                    return MovieDetails.Builder.from(movie).setVideos(videos).build();
                }
        ).toMaybe()
                .timeout(5, SECONDS)
                .doOnError(this::logError)
                .compose(applySchedulersToMaybe(schedulers));
    }

    @Override
    public Single<MovieDetails> save(@NonNull MovieDetails movie) {
        return movieDao.save(MovieMapper.toDto(movie))
                .doOnError(this::logError)
                .map(it -> MovieMapper.toMovieDetails(it, movie.getVideos()))
                .compose(applySchedulersToSingle(schedulers));
    }

}
