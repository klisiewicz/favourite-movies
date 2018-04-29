package pl.karollisiewicz.cinema.app.data.source.web;

import android.support.annotation.NonNull;

import java.util.Collection;

import pl.karollisiewicz.cinema.domain.movie.video.Video;

import static java.util.Collections.emptyList;

final class MovieMapper {
    private MovieMapper() {
    }

    @NonNull
    static pl.karollisiewicz.cinema.domain.movie.MovieDetails toMovieDetails(@NonNull final Movie movie) {
        return toMovieDetails(movie, emptyList());
    }

    @NonNull
    static pl.karollisiewicz.cinema.domain.movie.MovieDetails toMovieDetails(@NonNull final Movie movie,
                                                                             @NonNull final Collection<Video> videos) {
        return pl.karollisiewicz.cinema.domain.movie.MovieDetails.Builder.withId(movie.getId())
                .setTitle(movie.getTitle())
                .setOverview(movie.getOverview())
                .setRating(movie.getVoteAverage())
                .setReleaseDate(movie.getReleaseDate())
                .setBackdropUrl(movie.getBackdropPath())
                .setPosterUrl(movie.getPosterPath())
                .setFavourite(movie.isFavourite())
                .setVideos(videos)
                .build();
    }

    @NonNull
    static pl.karollisiewicz.cinema.domain.movie.Movie toMovie(@NonNull final Movie movie) {
        return new pl.karollisiewicz.cinema.domain.movie.Movie.Builder(movie.getId())
                .setTitle(movie.getTitle())
                .setBackdropUrl(movie.getBackdropPath())
                .setPosterUrl(movie.getPosterPath())
                .setReleaseDate(movie.getReleaseDate())
                .setRating(movie.getVoteAverage())
                .build();
    }

    @NonNull
    static Movie toDto(@NonNull final pl.karollisiewicz.cinema.domain.movie.Movie movie) {
        final Movie mov = new Movie();
        mov.setId(Long.valueOf(movie.getId().getValue()));
        mov.setTitle(movie.getTitle());
        mov.setBackdropPath(movie.getBackdropUrl());
        mov.setPosterPath(movie.getPosterUrl());
        return mov;
    }

    @NonNull
    static Movie toDto(@NonNull final pl.karollisiewicz.cinema.domain.movie.MovieDetails movie) {
        final Movie mov = new Movie();
        mov.setBackdropPath(movie.getBackdropUrl());
        mov.setId(Long.parseLong(movie.getId().getValue()));
        mov.setOverview(movie.getOverview());
        mov.setPosterPath(movie.getPosterUrl());
        mov.setReleaseDate(movie.getReleaseDate());
        mov.setTitle(movie.getTitle());
        mov.setVoteAverage(movie.getRating());
        mov.setFavourite(movie.isFavourite());
        return mov;
    }
}
