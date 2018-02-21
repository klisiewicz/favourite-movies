package pl.karollisiewicz.movie.app.dependency;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import pl.karollisiewicz.movie.app.MovieApplication;
import pl.karollisiewicz.movie.app.react.Schedulers;

/**
 * Module for application-wide dependencies.
 */
@Module
public class ApplicationModule {
    @Provides
    Context getContext(MovieApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    Schedulers getSchedulers() {
        return new Schedulers() {
            @Override
            public Scheduler getSubscriber() {
                return io.reactivex.schedulers.Schedulers.io();
            }

            @Override
            public Scheduler getObserver() {
                return AndroidSchedulers.mainThread();
            }
        };
    }
}
