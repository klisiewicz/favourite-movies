package pl.karollisiewicz.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

/**
 * This class is responsible for displaying {@link Snackbar} and guards that only one instance is displayed at given time.
 */
public final class SnackbarPresenter {
    private static final SnackbarPresenter INSTANCE = new SnackbarPresenter();

    @Nullable
    private Snackbar snackbar;

    private SnackbarPresenter() {
    }

    public static SnackbarPresenter getInstance() {
        return INSTANCE;
    }

    public void show(@NonNull final Snackbar snackbar) {
        if (this.snackbar == null) {
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    SnackbarPresenter.this.snackbar = null;
                }
            });
            snackbar.show();
            this.snackbar = snackbar;
        }
    }
}
