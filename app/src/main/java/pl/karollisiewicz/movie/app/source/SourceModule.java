package pl.karollisiewicz.movie.app.source;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.karollisiewicz.movie.domain.MovieRepository;

/**
 * Dependency module for all data sources related dependencies.
 */
@Module
public class SourceModule {
    @Provides
    @Singleton
    public MovieRepository getMovieRepository(@NonNull final MovieService movieService) {
        return new MovieWebRepository(movieService);
    }
}
