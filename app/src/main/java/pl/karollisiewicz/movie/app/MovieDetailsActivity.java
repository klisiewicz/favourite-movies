package pl.karollisiewicz.movie.app;

import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.karollisiewicz.common.ui.SnackbarPresenter;
import pl.karollisiewicz.movie.R;
import pl.karollisiewicz.movie.app.animation.TransitionNameSupplier;
import pl.karollisiewicz.movie.domain.Movie;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;
import static android.view.View.GONE;

public final class MovieDetailsActivity extends AppCompatActivity {
    private static final String MOVIE_KEY = "MovieDetailsActivity.Movie";

    @BindView(R.id.container_layout)
    ViewGroup container;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.backdrop_image)
    ImageView backdropImage;

    @BindView(R.id.poster_image)
    ImageView posterImage;

    @BindView(R.id.release_date_text)
    TextView releaseDateText;

    @BindView(R.id.average_vote_text)
    TextView averageRateText;

    @BindView(R.id.overview_text)
    TextView overviewText;

    @BindView(R.id.favourite_button)
    FloatingActionButton floatingActionButton;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Locale locale;

    @Inject
    SnackbarPresenter snackbarPresenter;
    private MovieDetailsViewModel viewModel;

    public static void start(@NonNull final Context context, @NonNull final ActivityOptions options,
                             @NonNull final Movie movie) {
        final Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_KEY, movie);
        context.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        AndroidInjection.inject(this);

        final Movie movie = getMovieFromIntent();
        setupActionBar(movie.getTitle());
        populateViewWith(movie);

        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(MovieDetailsViewModel.class);

        viewModel.getMovie().observe(this, movieResource -> {
            populateViewWith(movie);
            showSummaryMessage(movie);
        });

        floatingActionButton.setOnClickListener(v -> viewModel.toggleFavourite(movie));
    }

    private void showSummaryMessage(@NonNull final Movie movie) {
        int messageId = movie.isFavourite() ? R.string.favourite_added : R.string.favourite_removed;
        snackbarPresenter.show(
                make(container, getString(messageId), LENGTH_LONG)
                        .setAction(R.string.action_undo, v -> viewModel.toggleFavourite(movie))
        );
    }

    private Movie getMovieFromIntent() {
        final Intent intent = getIntent();
        if (intent == null) return new Movie.Builder().build();

        final Movie movie = (Movie) intent.getSerializableExtra(MOVIE_KEY);
        return movie != null ? movie : new Movie.Builder().build();
    }

    private void setupActionBar(final String title) {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void populateViewWith(Movie movie) {
        final String formattedDate = getFormattedDate(movie.getReleaseDate());
        releaseDateText.setText(formattedDate);
        averageRateText.setText(String.valueOf(movie.getRating()));
        overviewText.setText(movie.getOverview());
        posterImage.setTransitionName(TransitionNameSupplier.getInstance().apply(movie));
        floatingActionButton.setImageResource(movie.isFavourite() ? R.drawable.ic_favourite : R.drawable.ic_favourite_border);

        Picasso.with(this)
                .load(movie.getBackdropUrl())
                .into(backdropImage);

        Picasso.with(this)
                .load(movie.getPosterUrl())
                .into(posterImage);
    }

    private String getFormattedDate(@Nullable LocalDate date) {
        return date != null ? date.toString(DateTimeFormat.longDate().withLocale(locale)) : "";
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        floatingActionButton.setVisibility(GONE);
        super.onBackPressed();
    }
}
