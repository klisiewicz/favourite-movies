package pl.karollisiewicz.cinema.app.data.source.web.video;

import android.support.annotation.NonNull;

import io.reactivex.Single;

public final class VideoImageDecoratorService implements VideoService {
    @NonNull
    private final VideoWebService videoWebService;
    @NonNull
    private final VideoImageProvider imageProvider;

    public VideoImageDecoratorService(@NonNull final VideoWebService videoWebService,
                                      @NonNull final VideoImageProvider imageProvider) {

        this.videoWebService = videoWebService;
        this.imageProvider = imageProvider;
    }

    @Override
    public Single<Videos> fetchBy(long movieId) {
        return videoWebService
                .fetchBy(movieId)
                .toObservable()
                .map(Videos::getVideos)
                .flatMapIterable(list -> list)
                .map(this::updateUrls)
                .toList()
                .map(Videos::new);
    }

    @NonNull
    private Video updateUrls(@NonNull Video video) {
        video.setUrl(imageProvider.getVideoUrl(video.getKey()));
        video.setThumbnailUrl(imageProvider.getThumbnailUrl(video.getKey()));
        return video;
    }
}
