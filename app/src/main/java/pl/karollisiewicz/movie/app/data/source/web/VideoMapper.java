package pl.karollisiewicz.movie.app.data.source.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;

import io.reactivex.Observable;

import static pl.karollisiewicz.movie.domain.Video.Quality.FULL_HD;
import static pl.karollisiewicz.movie.domain.Video.Quality.HALF_HD;
import static pl.karollisiewicz.movie.domain.Video.Quality.SD;
import static pl.karollisiewicz.movie.domain.Video.Quality.ULTRA_HD;

class VideoMapper {
    private VideoMapper() {
    }

    @NonNull
    static pl.karollisiewicz.movie.domain.Video toDomain(@NonNull final Video video) {
        return new pl.karollisiewicz.movie.domain.Video.Builder()
                .setId(video.getId())
                .setKey(video.getKey())
                .setName(video.getName())
                .setType(byTypeName(video.getType()))
                .setQuality(byResolution(video.getSize()))
                .build();
    }

    @NonNull
    static Video toDto(@NonNull pl.karollisiewicz.movie.domain.Video video) {
        final Video vid = new Video();
        vid.setId(video.getId().getValue());
        vid.setType(video.getType() != null ? video.getType().name() : "");
        vid.setSite(video.getSite());
        vid.setName(video.getName());
        vid.setKey(video.getKey());
        return vid;
    }

    @Nullable
    private static pl.karollisiewicz.movie.domain.Video.Type byTypeName(final String name) {
        return Observable.fromIterable(Arrays.asList(pl.karollisiewicz.movie.domain.Video.Type.values()))
                .filter(it -> it.name().equalsIgnoreCase(name)).blockingFirst();
    }

    @Nullable
    private static pl.karollisiewicz.movie.domain.Video.Quality byResolution(final int resolution) {
        switch (resolution) {
            case 2160: return ULTRA_HD;
            case 1080: return FULL_HD;
            case 720: return HALF_HD;
            case 480:
            case 360:
            case 240: return SD;
            default: return null;
        }
    }
}
