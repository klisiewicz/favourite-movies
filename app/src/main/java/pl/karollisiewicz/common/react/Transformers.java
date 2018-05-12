package pl.karollisiewicz.common.react;

import android.support.annotation.NonNull;

import io.reactivex.MaybeTransformer;
import io.reactivex.SingleTransformer;

public final class Transformers {
    private Transformers() {
        throw new UnsupportedOperationException("No instances of this class should be created.");
    }

    public static <T> SingleTransformer<T, T> applySchedulersToSingle(@NonNull final Schedulers schedulers) {
        return single -> single
                .subscribeOn(schedulers.getSubscriber())
                .observeOn(schedulers.getObserver());
    }

    public static <T> MaybeTransformer<T, T> applySchedulersToMaybe(@NonNull final Schedulers schedulers) {
        return maybe -> maybe
                .subscribeOn(schedulers.getSubscriber())
                .observeOn(schedulers.getObserver());
    }
}
