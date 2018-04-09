package pl.karollisiewicz.movie.app.data.source.db;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static java.lang.String.valueOf;

/**
 * Dependency module holding database related dependencies.
 */
@Module
public final class DatabaseModule {

    @Provides
    @Singleton
    MovieDao getMovieDao(@NonNull final Context context,
                         @NonNull final MovieContentUriProvider uriProvider,
                         @NonNull final MovieContentValuesProvider contentValuesProvider) {
        return new MovieContentProviderAdapter(context.getContentResolver(), uriProvider, contentValuesProvider);
    }

    @Provides
    @Singleton
    MovieContentUriProvider getMovieContentUriProvider() {
        return new MovieContentUriProvider() {
            @Override
            public Uri getAll() {
                return MovieContentProvider.CONTENT_URI;
            }

            @Override
            public Uri getForId(long movieId) {
                return MovieContentProvider.CONTENT_URI.buildUpon().appendPath(valueOf(movieId)).build();
            }
        };
    }

    @Provides
    @Singleton
    MovieContentValuesProvider getMovieContentValuesProvider() {
        return movie -> MovieContentValuesBuilder
                .withMovie(movie)
                .build();
    }
}
