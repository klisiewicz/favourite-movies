package pl.karollisiewicz.cinema.app.data.source.web;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Single;

final class MovieCacheService implements MovieService {
    private final MovieService movieService;

    private Movies popularMovies;
    private Movies topRatedMovies;

    @Inject
    MovieCacheService(@NonNull final MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public Single<Movies> fetchPopular() {
        final Single<Movies> api = movieService.fetchPopular().doOnSuccess(movies -> popularMovies = movies);
        return popularMovies == null ? api : Single.just(popularMovies);
    }

    @Override
    public Single<Movies> fetchTopRated() {
        final Single<Movies> api = movieService.fetchTopRated().doOnSuccess(movies -> topRatedMovies = movies);
        return topRatedMovies == null ? api : Single.just(topRatedMovies);
    }

    @Override
    public Single<Movie> fetchById(String movieId) {
        return movieService.fetchById(movieId);
    }
}
