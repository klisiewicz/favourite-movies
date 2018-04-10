package pl.karollisiewicz.movie.app;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.Binds;
import dagger.MapKey;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Module for {@link android.arch.lifecycle.ViewModel} instances.
 */
@Module
public interface ViewModelModule {
    @IntoMap
    @Binds
    @ViewModelKey(MoviesViewModel.class)
    ViewModel moviesViewModel(MoviesViewModel viewModel);

    @IntoMap
    @Binds
    @ViewModelKey(MovieDetailsViewModel.class)
    ViewModel movieDetailsViewModel(MovieDetailsViewModel viewModel);

    @Binds
    ViewModelProvider.Factory viewModelFactory(ViewModelFactory factory);

    @Documented
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }
}
