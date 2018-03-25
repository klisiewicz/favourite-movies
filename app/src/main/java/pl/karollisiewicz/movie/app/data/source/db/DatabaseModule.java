package pl.karollisiewicz.movie.app.data.source.db;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency module holding database related dependencies.
 */
@Module
public final class DatabaseModule {

    @Provides
    @Singleton
    MovieDao getMovieDao(@NonNull final Context context) {
        return new MovieContentProviderAdapter(context);
    }
}
