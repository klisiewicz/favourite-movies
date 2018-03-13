package pl.karollisiewicz.react;

import io.reactivex.Scheduler;

/**
 * Schedulers required for reactive programming.
 */
public interface Schedulers {
    Scheduler getSubscriber();

    Scheduler getObserver();
}
