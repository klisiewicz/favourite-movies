package pl.karollisiewicz.cinema.app.data.source.web;

import android.support.annotation.NonNull;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import pl.karollisiewicz.cinema.app.data.source.db.MovieDao;
import pl.karollisiewicz.cinema.domain.exception.AuthorizationException;
import pl.karollisiewicz.cinema.domain.exception.CommunicationException;
import pl.karollisiewicz.cinema.domain.movie.MovieId;
import pl.karollisiewicz.cinema.domain.movie.MovieRepository;
import pl.karollisiewicz.common.log.Logger;
import pl.karollisiewicz.common.react.Schedulers;
import retrofit2.HttpException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static pl.karollisiewicz.common.react.Transformers.applySchedulers;

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
    public Single<List<pl.karollisiewicz.cinema.domain.movie.Movie>> fetchBy(@NonNull Criterion criterion) {
        return Single.zip(
                getMoviesMatchingCriterion(criterion)
                        .toObservable()
                        .map(Movies::getMovies)
                        .flatMapIterable(list -> list)
                        .map(MovieMapper::toDomain)
                        .toList(),
                movieDao.fetchFavourites(),
                this::markFavourites
        )
                .timeout(5, SECONDS)
                .doOnError(this::logError)
                .onErrorResumeNext(this::mapError)
                .compose(applySchedulers(schedulers));
    }

    private Single<Movies> getMoviesMatchingCriterion(@NonNull final Criterion criterion) {
        if (Criterion.POPULARITY == criterion) return movieService.fetchPopular();
        else if (Criterion.RATING == criterion) return movieService.fetchTopRated();
        else if (Criterion.FAVOURITE == criterion) return movieDao.fetchFavourites().map(movies -> new Movies(new ArrayList<>(movies)));
        else return Single.never();
    }

    @NonNull
    private List<pl.karollisiewicz.cinema.domain.movie.Movie> markFavourites(List<pl.karollisiewicz.cinema.domain.movie.Movie> movies, Collection<pl.karollisiewicz.cinema.app.data.source.web.Movie> favourites) {
        for (pl.karollisiewicz.cinema.domain.movie.Movie movie : movies)
            for (pl.karollisiewicz.cinema.app.data.source.web.Movie favourite : favourites)
                if (movie.getId().equals(MovieId.of(favourite.getId())))
                    movie.favourite();
        return movies;
    }

    private void logError(Throwable throwable) {
        logger.error(MovieWebRepository.class, throwable);
    }

    private SingleSource<? extends List<pl.karollisiewicz.cinema.domain.movie.Movie>> mapError(Throwable throwable) {
        if (throwable instanceof UnknownHostException || throwable instanceof TimeoutException)
            return Single.error(new CommunicationException(throwable));
        else if (throwable instanceof HttpException && ((HttpException) throwable).code() == CODE_UNAUTHORIZED)
            return Single.error(new AuthorizationException(throwable));
        else return Single.error(throwable);
    }

    @Override
    public Single<pl.karollisiewicz.cinema.domain.movie.Movie> save(@NonNull pl.karollisiewicz.cinema.domain.movie.Movie movie) {
        return movieDao.save(MovieMapper.toDto(movie))
                .doOnError(this::logError)
                .map(MovieMapper::toDomain)
                .compose(applySchedulers(schedulers));
    }

}
