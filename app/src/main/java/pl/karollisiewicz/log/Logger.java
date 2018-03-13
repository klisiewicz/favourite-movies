package pl.karollisiewicz.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Logger {
    void debug(@NonNull final Class<?> clazz, @Nullable String message);

    void info(@NonNull final Class<?> clazz, @Nullable String message);

    void warning(@NonNull final Class<?> clazz, @Nullable String message);

    void error(@NonNull final Class<?> clazz, @NonNull Throwable throwable);
}
