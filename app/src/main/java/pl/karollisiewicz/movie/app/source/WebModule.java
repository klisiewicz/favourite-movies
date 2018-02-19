package pl.karollisiewicz.movie.app.source;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

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
    public MovieService getMovieService(Retrofit retrofit) {
        return retrofit.create(MovieService.class);
    }

    @Provides
    @Singleton
    Retrofit getRetrofit(@Named("api-url") String apiUrl, final Gson gson, OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient getHttpClient(@Named("api-key") @NonNull String apiKey) {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BODY);

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HeaderInterceptor());
        builder.addInterceptor(new ApiKeyInterceptor(apiKey));
        builder.addInterceptor(loggingInterceptor);

        return builder.build();
    }

    private static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            final Request request = chain.request()
                    .newBuilder()
                    .addHeader("Accept", "application/json")
                    .build();

            return chain.proceed(request);
        }
    }

    private static class ApiKeyInterceptor implements Interceptor {
        private final String apiKey;

        private ApiKeyInterceptor(@NonNull final String apiKey) {
            this.apiKey = apiKey;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            final HttpUrl url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", apiKey)
                    .build();

            final Request request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build();

            return chain.proceed(request);
        }
    }

}
