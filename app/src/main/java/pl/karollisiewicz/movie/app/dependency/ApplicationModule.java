package pl.karollisiewicz.movie.app.dependency;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import pl.karollisiewicz.movie.app.MovieApplication;

/**
 * Module for application-wide dependencies.
 */
@Module
public class ApplicationModule {
    @Provides
    Context context(MovieApplication application) {
        return application.getApplicationContext();
    }
}
