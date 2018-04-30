package pl.karollisiewicz.cinema.app.data.source.web;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * MovieDetails service responsible for updating values of {@link Movie#backdropPath} and {@link Movie#posterPath} with the values given by the
 * {@link MovieImageProvider}.
 */
public final class MovieImageDecoratorService implements MovieService {

    @NonNull
    private final MovieImageProvider movieImageProvider;

    @NonNull
    private final MovieWebService movieService;

    @Inject
    MovieImageDecoratorService(@NonNull final MovieWebService movieService,
                               @NonNull final MovieImageProvider movieImageProvider) {

        this.movieService = movieService;
        this.movieImageProvider = movieImageProvider;
    }

    @Override
    public Single<Movie> fetchById(String movieId) {
        return movieService.fetchById(movieId)
                .map(this::updateUrls);
    }

    @Override
    public Single<Movies> fetchPopular() {
        return getMoviesWithUpdatedUrl(movieService.fetchPopular());
    }

    @Override
    public Single<Movies> fetchTopRated() {
        return getMoviesWithUpdatedUrl(movieService.fetchTopRated());
    }

    private Single<Movies> getMoviesWithUpdatedUrl(Single<Movies> movies) {
        return movies.toObservable()
                .map(Movies::getMovies)
                .flatMapIterable(list -> list)
                .map(this::updateUrls)
                .toList()
                .map(Movies::new);
    }

    @NonNull
    private Movie updateUrls(@NonNull Movie movie) {
        movie.setBackdropPath(movieImageProvider.getBackdropUrl(movie.getBackdropPath()));
        movie.setPosterPath(movieImageProvider.getPosterUrl(movie.getPosterPath()));
        return movie;
    }
}
