package pl.karollisiewicz.cinema.app.dependency;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.karollisiewicz.cinema.app.ui.movie.MoviesFragment;

/**
 * Module for all fragments within the app.
 */
@Module
public interface FragmentModule {
    @ContributesAndroidInjector
    MoviesFragment bindMoviesFragment();
}
