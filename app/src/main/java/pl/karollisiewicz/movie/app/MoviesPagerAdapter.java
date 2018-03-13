package pl.karollisiewicz.movie.app;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pl.karollisiewicz.movie.R;

public final class MoviesPagerAdapter extends FragmentStatePagerAdapter {
    private static final int PAGE_NUMBER = 2;

    private final Context context;

    MoviesPagerAdapter(final Context context, final FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return MoviesFragment.newPopularInstance();
        else if (position == 1) return MoviesFragment.newTopRatedInstance();
        else return null;
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return context.getString(R.string.category_popular);
        else if (position == 1) return context.getString(R.string.category_top_rated);
        else return null;
    }
}
