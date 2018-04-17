package pl.karollisiewicz.movie.app;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pl.karollisiewicz.common.log.Logger;

/**
 * Mock of the {@link pl.karollisiewicz.common.log.Logger} interface used for tests.
 */
public final class ConsoleLogger implements Logger {
    @Override
    public void debug(@NonNull Class<?> clazz, @Nullable String message) {
        System.out.println(String.format("%s: %s", clazz.getName(), message));
    }

    @Override
    public void info(@NonNull Class<?> clazz, @Nullable String message) {
        System.out.println(String.format("%s: %s", clazz.getName(), message));
    }

    @Override
    public void warning(@NonNull Class<?> clazz, @Nullable String message) {
        System.out.println(String.format("%s: %s", clazz.getName(), message));
    }

    @Override
    public void error(@NonNull Class<?> clazz, @NonNull Throwable throwable) {
        System.out.print(clazz.getName() + ": ");
        throwable.printStackTrace(System.out);
    }
}
