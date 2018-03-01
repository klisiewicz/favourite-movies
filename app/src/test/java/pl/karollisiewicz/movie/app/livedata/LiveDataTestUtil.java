package pl.karollisiewicz.movie.app.livedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;

public final class LiveDataTestUtil {
    private LiveDataTestUtil() {
    }

    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Holder<T> holder = new Holder<>();
        final CountDownLatch latch = new CountDownLatch(1);
        final Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T value) {
                holder.value = value;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, SECONDS);

        return holder.value;
    }

    private static final class Holder<T> {
        T value;
    }
}
