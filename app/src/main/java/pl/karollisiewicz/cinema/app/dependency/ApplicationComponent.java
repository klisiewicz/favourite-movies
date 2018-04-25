package pl.karollisiewicz.cinema.app.dependency;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import pl.karollisiewicz.cinema.app.MovieApplication;
import pl.karollisiewicz.cinema.app.ViewModelModule;
import pl.karollisiewicz.cinema.app.config.ConfigurationModule;
import pl.karollisiewicz.cinema.app.data.source.SourceModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        ActivityModule.class,
        ConfigurationModule.class,
        FragmentModule.class,
        SourceModule.class,
        ViewModelModule.class,
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
