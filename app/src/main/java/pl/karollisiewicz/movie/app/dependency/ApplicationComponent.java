package pl.karollisiewicz.movie.app.dependency;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import pl.karollisiewicz.movie.app.MovieApplication;
import pl.karollisiewicz.movie.app.ViewModelModule;
import pl.karollisiewicz.movie.app.config.ConfigurationModule;
import pl.karollisiewicz.movie.app.source.SourceModule;
import pl.karollisiewicz.movie.app.source.WebModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        ActivityModule.class,
        ConfigurationModule.class,
        FragmentModule.class,
        SourceModule.class,
        ViewModelModule.class,
        WebModule.class
})
public interface ApplicationComponent {
    void inject(MovieApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MovieApplication application);

        ApplicationComponent build();
    }
}
