package pl.karollisiewicz.movie.app.dependency;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import pl.karollisiewicz.movie.app.MovieApplication;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        ActivityModule.class})
public interface ApplicationComponent {
    void inject(MovieApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MovieApplication application);

        ApplicationComponent build();
    }
}
