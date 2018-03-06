package pl.karollisiewicz.movie.app.dependency;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.karollisiewicz.movie.app.MovieDetailsActivity;

/**
 * Module for all activities within the app.
 */
@Module
public interface ActivityModule {
    @ContributesAndroidInjector
    MovieDetailsActivity bindMovieDetailsActivity();
}
