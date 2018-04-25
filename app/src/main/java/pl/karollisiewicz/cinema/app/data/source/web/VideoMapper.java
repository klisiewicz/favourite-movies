package pl.karollisiewicz.cinema.app.data.source.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;

import io.reactivex.Observable;

class VideoMapper {
    private VideoMapper() {
    }

    @NonNull
    static pl.karollisiewicz.cinema.domain.Video toDomain(@NonNull final Video video) {
        return new pl.karollisiewicz.cinema.domain.Video.Builder()
                .setId(video.getId())
                .setKey(video.getKey())
                .setName(video.getName())
                .setType(byTypeName(video.getType()))
                .setQuality(byResolution(video.getSize()))
                .build();
    }

    @NonNull
    static Video toDto(@NonNull pl.karollisiewicz.cinema.domain.Video video) {
        final Video vid = new Video();
        vid.setId(video.getId().getValue());
        vid.setType(video.getType() != null ? video.getType().name() : "");
        vid.setSite(video.getSite());
        vid.setName(video.getName());
        vid.setKey(video.getKey());
        return vid;
    }

    @Nullable
    private static pl.karollisiewicz.cinema.domain.Video.Type byTypeName(final String name) {
        return Observable.fromIterable(Arrays.asList(pl.karollisiewicz.cinema.domain.Video.Type.values()))
                .filter(it -> it.name().equalsIgnoreCase(name)).blockingFirst();
    }

    @Nullable
    private static pl.karollisiewicz.cinema.domain.Video.Quality byResolution(final int resolution) {
        switch (resolution) {
            case 2160: return pl.karollisiewicz.cinema.domain.Video.Quality.ULTRA_HD;
            case 1080: return pl.karollisiewicz.cinema.domain.Video.Quality.FULL_HD;
            case 720: return pl.karollisiewicz.cinema.domain.Video.Quality.HALF_HD;
            case 480:
            case 360:
            case 240: return pl.karollisiewicz.cinema.domain.Video.Quality.SD;
            default: return null;
        }
    }
}
