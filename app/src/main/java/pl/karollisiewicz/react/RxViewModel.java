package pl.karollisiewicz.react;

import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class RxViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();

    protected void add(Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    public void onCleared() {
        disposables.clear();
    }
}
