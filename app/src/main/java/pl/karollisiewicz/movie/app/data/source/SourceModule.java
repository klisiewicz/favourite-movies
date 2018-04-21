package pl.karollisiewicz.movie.app.data.source;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.karollisiewicz.common.log.Logger;
import pl.karollisiewicz.common.react.Schedulers;
import pl.karollisiewicz.movie.app.data.source.db.DatabaseModule;
import pl.karollisiewicz.movie.app.data.source.db.MovieDao;
import pl.karollisiewicz.movie.app.data.source.web.MovieService;
import pl.karollisiewicz.movie.app.data.source.web.MovieWebRepository;
import pl.karollisiewicz.movie.app.data.source.web.WebModule;
import pl.karollisiewicz.movie.domain.MovieRepository;

/**
 * Dependency module for all data sources related dependencies.
 */
@Module(includes = {WebModule.class, DatabaseModule.class})
public final class SourceModule {

    @Provides
    @Singleton
    MovieRepository getMovieRepository(
            @NonNull final MovieService movieService,
            @NonNull final MovieDao movieDao,
            @NonNull final Schedulers schedulers,
            @NonNull final Logger logger) {
        return new MovieWebRepository(movieService, movieDao, schedulers, logger);
    }
}