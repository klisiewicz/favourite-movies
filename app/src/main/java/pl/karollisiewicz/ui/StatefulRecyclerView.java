package pl.karollisiewicz.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Recycler view that is capable of storing and restoring it's state.
 */
public final class StatefulRecyclerView extends RecyclerView {
    private static final String SAVED_SUPER_STATE = "StatefulRecyclerView.SuperState";
    private static final String SAVED_LAYOUT_MANAGER = "StatefulRecyclerView.LayoutState";

    private Parcelable layoutManagerState;

    public StatefulRecyclerView(Context context) {
        super(context);
    }

    public StatefulRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatefulRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(SAVED_SUPER_STATE, super.onSaveInstanceState());
        bundle.putParcelable(SAVED_LAYOUT_MANAGER, this.getLayoutManager().onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            layoutManagerState = bundle.getParcelable(SAVED_LAYOUT_MANAGER);
            state = bundle.getParcelable(SAVED_SUPER_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        restorePosition();
    }

    private void restorePosition() {
        if (layoutManagerState != null) {
            getLayoutManager().onRestoreInstanceState(layoutManagerState);
            layoutManagerState = null;
        }
    }
}
