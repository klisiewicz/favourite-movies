package pl.karollisiewicz.movie.app.source;

import android.support.annotation.NonNull;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.karollisiewicz.movie.app.react.Schedulers;
import pl.karollisiewicz.movie.domain.MovieRepository;

/**
 * Dependency module for all data sources related dependencies.
 */
@Module
public class SourceModule {
    @Provides
    @Singleton
    MovieRepository getMovieRepository(@NonNull @Named("image-url") String imageUrl,
                                       @NonNull final MovieService movieService,
                                       @NonNull final Schedulers schedulers) {
        return new MovieWebRepository(imageUrl, movieService, schedulers);
    }
}
