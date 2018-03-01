package pl.karollisiewicz.movie.app.source;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
                .serializeNulls()
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient getHttpClient(final @Named("api-key") @NonNull String apiKey, final @NonNull Locale locale) {
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
