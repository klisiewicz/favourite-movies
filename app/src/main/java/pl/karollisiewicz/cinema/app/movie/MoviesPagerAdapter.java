package pl.karollisiewicz.cinema.app.movie;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pl.karollisiewicz.cinema.R;

public final class MoviesPagerAdapter extends FragmentStatePagerAdapter {
    private static final int PAGE_NUMBER = 3;

    private final Context context;

    MoviesPagerAdapter(final Context context, final FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return MoviesFragment.newPopularInstance();
        else if (position == 1) return MoviesFragment.newTopRatedInstance();
        else if (position == 2) return MoviesFragment.newFavouriteInstance();
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
        else if (position == 2) return context.getString(R.string.category_favourite);
        else return null;
    }
}
