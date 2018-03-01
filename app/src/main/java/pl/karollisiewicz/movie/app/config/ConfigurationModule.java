package pl.karollisiewicz.movie.app.config;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import pl.karollisiewicz.movie.BuildConfig;

/**
 * Dependency module that holds all configurable constants.
 */
@Module
public final class ConfigurationModule {

    @Provides
    @Named("api-url")
    String getApiUrl() {
        return BuildConfig.API_URL;
    }

    @Provides
    @Named("api-key")
    String getApiKey() {
        return BuildConfig.API_KEY;
    }

    @Provides
    @Named("image-url")
    String getImageUrl() {
        return BuildConfig.IMAGE_URL;
    }

    @Provides
    @Named("is-debug")
    boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
