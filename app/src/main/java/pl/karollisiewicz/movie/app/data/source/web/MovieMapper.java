package pl.karollisiewicz.movie.app.data.source.web;

import android.support.annotation.NonNull;

import java.util.Collection;

import io.reactivex.Observable;

import static java.util.Collections.emptyList;
import static pl.karollisiewicz.common.collection.CollectionUtils.newCollection;

final class MovieMapper {
    private MovieMapper() {
    }

    @NonNull
    static pl.karollisiewicz.movie.domain.Movie toDomain(@NonNull final Movie movie) {
        return toDomain(movie, emptyList());
    }

    @NonNull
    static pl.karollisiewicz.movie.domain.Movie toDomain(@NonNull final Movie movie, @NonNull final Collection<Video> videos) {
        return new pl.karollisiewicz.movie.domain.Movie.Builder(movie.getId())
                .setTitle(movie.getTitle())
                .setOverview(movie.getOverview())
                .setRating(movie.getVoteAverage())
                .setReleaseDate(movie.getReleaseDate())
                .setBackdropUrl(movie.getBackdropPath())
                .setPosterUrl(movie.getPosterPath())
                .setFavourite(movie.isFavourite())
                .setVideos(newCollection(Observable.fromIterable(videos)
                        .map(VideoMapper::toDomain)
                        .blockingIterable()))
                .build();
    }

    @NonNull
    static Movie toDto(@NonNull final pl.karollisiewicz.movie.domain.Movie movie) {
        final Movie mov = new Movie();
        mov.setBackdropPath(movie.getBackdropUrl());
        mov.setId(Long.valueOf(movie.getId().getValue()));
        mov.setOverview(movie.getOverview());
        mov.setPosterPath(movie.getPosterUrl());
        mov.setReleaseDate(movie.getReleaseDate());
        mov.setTitle(movie.getTitle());
        mov.setVoteAverage(movie.getRating());
        mov.setFavourite(movie.isFavourite());
        return mov;
    }
}
