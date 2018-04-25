package pl.karollisiewicz.cinema.app.dependency;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.karollisiewicz.cinema.app.MovieDetailsActivity;

/**
 * Module for all activities within the app.
 */
@Module
public interface ActivityModule {
    @ContributesAndroidInjector
    MovieDetailsActivity bindMovieDetailsActivity();
}
