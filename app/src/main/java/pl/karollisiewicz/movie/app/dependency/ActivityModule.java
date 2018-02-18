package pl.karollisiewicz.movie.app.dependency;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.karollisiewicz.movie.app.MovieDetailsActivity;
import pl.karollisiewicz.movie.app.MoviesActivity;

/**
 * Module for all activities within the app.
 */
@Module
public interface ActivityModule {
    @ContributesAndroidInjector
    MoviesActivity bindMoviesActivity();

    @ContributesAndroidInjector
    MovieDetailsActivity bindMovieDetailsActivity();
}
