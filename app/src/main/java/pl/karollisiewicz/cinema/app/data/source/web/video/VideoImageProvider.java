package pl.karollisiewicz.cinema.app.data.source.web.video;

import android.support.annotation.NonNull;

public interface VideoImageProvider {
    String getVideoUrl(@NonNull String key);

    String getThumbnailUrl(@NonNull String key);
}
