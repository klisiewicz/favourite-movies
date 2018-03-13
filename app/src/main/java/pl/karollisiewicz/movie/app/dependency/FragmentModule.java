package pl.karollisiewicz.movie.app.dependency;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.karollisiewicz.movie.app.MoviesFragment;

/**
 * Module for all fragments within the app.
 */
@Module
public interface FragmentModule {
    @ContributesAndroidInjector
    MoviesFragment bindMoviesFragment();
}
