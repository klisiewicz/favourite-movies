package pl.karollisiewicz.cinema.app.data.source.web;

import android.support.annotation.NonNull;

import pl.karollisiewicz.cinema.BuildConfig;

final class BuildConfigVideoImageProvider implements VideoImageProvider {
    @Override
    public String getVideoUrl(@NonNull final String key) {
        return String.format("%s%s", BuildConfig.YOUTUBE_URL, key);
    }

    @Override
    public String getThumbnailUrl(@NonNull final String key) {
        return String.format("%s%s%s", BuildConfig.YOUTUBE_THUMBNAIL_URL, key, "/0.jpg");
    }
}
