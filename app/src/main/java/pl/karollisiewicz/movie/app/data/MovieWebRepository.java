package pl.karollisiewicz.movie.app.data;

import android.support.annotation.NonNull;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import pl.karollisiewicz.common.log.Logger;
import pl.karollisiewicz.common.react.Schedulers;
import pl.karollisiewicz.movie.app.data.source.db.MovieDao;
import pl.karollisiewicz.movie.app.data.source.web.MovieService;
import pl.karollisiewicz.movie.app.data.source.web.MovieWebService;
import pl.karollisiewicz.movie.app.data.source.web.Movies;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository;
import pl.karollisiewicz.movie.domain.exception.AuthorizationException;
import pl.karollisiewicz.movie.domain.exception.CommunicationException;
import retrofit2.HttpException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static pl.karollisiewicz.common.react.Transformers.applySchedulers;
import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.FAVOURITE;
import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.POPULARITY;
import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.RATING;

/**
 * Repository that utilizes {@link MovieWebService} web service to fetch movies.
 */
public final class MovieWebRepository implements MovieRepository {

    private static final int CODE_UNAUTHORIZED = 401;

    private final MovieService movieService;
    private final MovieDao movieDao;
    private final Schedulers schedulers;
    private final Logger logger;
    private final MovieMapper movieMapper;

    @Inject
    MovieWebRepository(@NonNull final MovieService movieService,
                       @NonNull final MovieDao movieDao,
                       @NonNull final Schedulers schedulers,
                       @NonNull final Logger logger) {
        this.movieMapper = new MovieMapper();
        this.movieService = movieService;
        this.movieDao = movieDao;
        this.schedulers = schedulers;
        this.logger = logger;
    }

    @Override
    public Single<List<Movie>> fetchBy(@NonNull Criterion criterion) {
        return Single.zip(
                movieDao.fetchFavourites(),
                getMoviesMatchingCriterion(criterion)
                        .toObservable()
                        .map(Movies::getMovies)
                        .flatMapIterable(list -> list)
                        .map(movieMapper::toDomain)
                        .toList(),
                (favourites, movies) -> {
                    for (Movie movie : movies)
                        for (pl.karollisiewicz.movie.app.data.source.web.Movie favourite : favourites)
                            if (movie.getId().getValue().equals(String.valueOf(favourite.getId())))
                                movie.favourite();
                    return movies;
                }
        )
                .timeout(3, SECONDS)
                .doOnError(this::logError)
                .onErrorResumeNext(this::mapError)
                .compose(applySchedulers(schedulers));
    }

    private Single<Movies> getMoviesMatchingCriterion(@NonNull final Criterion criterion) {
        if (POPULARITY == criterion) return movieService.fetchPopular();
        else if (RATING == criterion) return movieService.fetchTopRated();
        else if (FAVOURITE == criterion) return movieDao.fetchFavourites().map(movies -> new Movies(new ArrayList<>(movies)));
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
    public Single<Movie> save(@NonNull Movie movie) {
        return movieDao.save(movieMapper.toDto(movie))
                .doOnError(this::logError)
                .map(movieMapper::toDomain)
                .compose(applySchedulers(schedulers));
    }

    private static final class MovieMapper {

        @NonNull
        Movie toDomain(@NonNull final pl.karollisiewicz.movie.app.data.source.web.Movie movie) {
            return new Movie.Builder(movie.getId())
                    .setTitle(movie.getTitle())
                    .setOverview(movie.getOverview())
                    .setRating(movie.getVoteAverage())
                    .setReleaseDate(movie.getReleaseDate())
                    .setBackdropUrl(movie.getBackdropPath())
                    .setPosterUrl(movie.getPosterPath())
                    .setFavourite(movie.isFavourite())
                    .build();
        }

        @NonNull
        pl.karollisiewicz.movie.app.data.source.web.Movie toDto(@NonNull final Movie movie) {
            final pl.karollisiewicz.movie.app.data.source.web.Movie webMovie = new pl.karollisiewicz.movie.app.data.source.web.Movie();
            webMovie.setBackdropPath(movie.getBackdropUrl());
            webMovie.setId(Long.valueOf(movie.getId().getValue()));
            webMovie.setOverview(movie.getOverview());
            webMovie.setPosterPath(movie.getPosterUrl());
            webMovie.setReleaseDate(movie.getReleaseDate());
            webMovie.setTitle(movie.getTitle());
            webMovie.setVoteAverage(movie.getRating());
            webMovie.setFavourite(movie.isFavourite());

            return webMovie;
        }
    }
}
