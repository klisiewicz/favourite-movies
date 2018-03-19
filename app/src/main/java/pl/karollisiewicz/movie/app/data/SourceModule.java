package pl.karollisiewicz.movie.app.data;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.karollisiewicz.log.Logger;
import pl.karollisiewicz.movie.BuildConfig;
import pl.karollisiewicz.movie.app.data.source.web.MovieService;
import pl.karollisiewicz.movie.domain.MovieRepository;
import pl.karollisiewicz.react.Schedulers;

/**
 * Dependency module for all data sources related dependencies.
 */
@Module
public class SourceModule {
    @Provides
    @Singleton
    public MovieImageProvider getImageProvider() {
        return new MovieImageProvider() {
            @Override
            public String getPosterUrl(String resourceUrl) {
                return String.format("%s%s", BuildConfig.POSTER_URL, resourceUrl);
            }

            @Override
            public String getBackdropUrl(String resourceUrl) {
                return String.format("%s%s", BuildConfig.BACKDROP_URL, resourceUrl);
            }
        };
    }

    @Provides
    @Singleton
    public MovieRepository getMovieRepository(@NonNull final MovieImageProvider movieImageProvider,
                                              @NonNull final MovieService movieService,
                                              @NonNull final Schedulers schedulers,
                                              @NonNull final Logger logger) {
        return new MovieWebRepository(movieImageProvider, movieService, schedulers, logger);
    }
}
