package pl.karollisiewicz.movie.app.dependency;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import pl.karollisiewicz.log.Logger;
import pl.karollisiewicz.movie.app.MovieApplication;
import pl.karollisiewicz.react.Schedulers;
import pl.karollisiewicz.ui.SnackbarPresenter;

/**
 * Module for application-wide dependencies.
 */
@Module
final class ApplicationModule {
    @Provides
    Context getContext(MovieApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
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

    @Provides
    @Singleton
    Logger getLogger() {
        return new Logger() {

            @Override
            public void debug(@NonNull Class<?> clazz, @Nullable String message) {
                Log.d(clazz.getName(), message);
            }

            @Override
            public void info(@NonNull Class<?> clazz, @Nullable String message) {
                Log.i(clazz.getName(), message);
            }

            @Override
            public void warning(@NonNull Class<?> clazz, @Nullable String message) {
                Log.w(clazz.getName(), message);
            }

            @Override
            public void error(@NonNull Class<?> clazz, @NonNull Throwable throwable) {
                Log.e(clazz.getName(), "", throwable);
            }
        };
    }

    @Provides
    @Singleton
    Locale getLocale(@NonNull final Context context) {
        return context.getResources().getConfiguration().locale;
    }

    @Provides
    @Singleton
    SnackbarPresenter getSnackbarManager() {
        return SnackbarPresenter.getInstance();
    }
}
