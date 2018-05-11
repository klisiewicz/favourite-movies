package pl.karollisiewicz.cinema.app.data.source.web;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.LocalDate;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.karollisiewicz.cinema.app.data.source.web.review.ReviewService;
import pl.karollisiewicz.cinema.app.data.source.web.video.BuildConfigVideoImageProvider;
import pl.karollisiewicz.cinema.app.data.source.web.video.VideoImageDecoratorService;
import pl.karollisiewicz.cinema.app.data.source.web.video.VideoImageProvider;
import pl.karollisiewicz.cinema.app.data.source.web.video.VideoService;
import pl.karollisiewicz.cinema.app.data.source.web.video.VideoWebService;
import pl.karollisiewicz.common.log.Logger;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * Dependency module that holds dependencies required for network requests.
 */
@Module
public final class WebModule {

    @Provides
    @Singleton
    @Named("MovieCacheService")
    public MovieService getMovieCacheService(@Named("MovieImageDecoratorService") MovieService movieService) {
        return new MovieCacheService(movieService);
    }

    @Provides
    @Singleton
    @Named("MovieImageDecoratorService")
    public MovieService getMovieService(MovieWebService movieWebService, MovieImageProvider imageProvider) {
        return new MovieImageDecoratorService(movieWebService, imageProvider);
    }

    @Provides
    @Singleton
    MovieImageProvider getMovieImageProvider() {
        return new BuildConfigMovieImageProvider();
    }

    @Provides
    @Singleton
    public MovieWebService getMovieWebService(Retrofit retrofit) {
        return retrofit.create(MovieWebService.class);
    }

    @Provides
    @Singleton
    public VideoService getVideoService(VideoWebService videoWebService, VideoImageProvider imageProvider) {
        return new VideoImageDecoratorService(videoWebService, imageProvider);
    }

    @Provides
    @Singleton
    VideoImageProvider getVideoImageProvider() {
        return new BuildConfigVideoImageProvider();
    }

    @Provides
    @Singleton
    public VideoWebService getVideoWebService(Retrofit retrofit) {
        return retrofit.create(VideoWebService.class);
    }

    @Provides
    @Singleton
    public ReviewService getReviewService(Retrofit retrofit) {
        return retrofit.create(ReviewService.class);
    }

    @Provides
    @Singleton
    public Retrofit getRetrofit(@Named("api-url") String apiUrl, final Gson gson, OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    Gson getGson(Logger loggger) {
        return new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .registerTypeAdapter(LocalDate.class, new DateJsonDeserializer(loggger))
                .create();
    }

    @Provides
    @Singleton
    public OkHttpClient getHttpClient(final @Named("api-key") @NonNull String apiKey, final @NonNull Locale locale) {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BODY);

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HeaderInterceptor(locale));
        builder.addInterceptor(new QueryInterceptor(apiKey, locale));
        builder.addInterceptor(loggingInterceptor);

        return builder.build();
    }

    private static class HeaderInterceptor implements Interceptor {
        private final Locale locale;

        private HeaderInterceptor(@NonNull final Locale locale) {
            this.locale = locale;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            final Request request = chain.request()
                    .newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Language", locale.getLanguage())
                    .build();

            return chain.proceed(request);
        }
    }

    private static class QueryInterceptor implements Interceptor {
        private final String apiKey;
        private final Locale locale;

        private QueryInterceptor(@NonNull final String apiKey, @NonNull final Locale locale) {
            this.apiKey = apiKey;
            this.locale = locale;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            final HttpUrl url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", apiKey)
                    .addQueryParameter("language", locale.getLanguage())
                    .addQueryParameter("region", locale.getCountry())
                    .build();

            final Request request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build();

            return chain.proceed(request);
        }
    }

}
