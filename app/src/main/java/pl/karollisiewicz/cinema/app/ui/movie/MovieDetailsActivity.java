package pl.karollisiewicz.cinema.app.ui.movie;

import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.Collection;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.karollisiewicz.cinema.R;
import pl.karollisiewicz.cinema.app.animation.TransitionNameSupplier;
import pl.karollisiewicz.cinema.app.ui.movie.video.VideosAdapter;
import pl.karollisiewicz.cinema.domain.exception.AuthorizationException;
import pl.karollisiewicz.cinema.domain.exception.CommunicationException;
import pl.karollisiewicz.cinema.domain.movie.Movie;
import pl.karollisiewicz.cinema.domain.movie.MovieDetails;
import pl.karollisiewicz.cinema.domain.movie.MovieId;
import pl.karollisiewicz.cinema.domain.movie.review.Review;
import pl.karollisiewicz.common.ui.Resource;
import pl.karollisiewicz.common.ui.SnackbarPresenter;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;
import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static pl.karollisiewicz.common.ui.Resource.Status.ERROR;
import static pl.karollisiewicz.common.ui.Resource.Status.SUCCESS;

public final class MovieDetailsActivity extends AppCompatActivity {
    private static final String MOVIE_KEY = "MovieDetailsActivity.MovieDetails";

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

    @BindView(R.id.reviews_number_text)
    TextView reviewsNumberText;

    @BindView(R.id.review_author_text)
    TextView reviewAuthor;

    @BindView(R.id.review_text)
    TextView reviewText;

    @BindView(R.id.browse_reviews_button)
    Button reviewsBrowse;

    @BindView(R.id.favourite_button)
    FloatingActionButton floatingActionButton;

    @Inject
    Locale locale;

    @Inject
    SnackbarPresenter snackbarPresenter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private MovieDetailsViewModel viewModel;

    @BindView(R.id.videos_list)
    RecyclerView videoList;

    private VideosAdapter videosAdapter;

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
        setupRecyclerView();
        setupViewModel(movie.getId());
        populateViewWith(movie);

        floatingActionButton.setOnClickListener(v -> viewModel.toggleFavourite());
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

    private void setupRecyclerView() {
        videosAdapter = new VideosAdapter();
        videosAdapter.setVideoClickListener(video -> {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getUrl()));
            if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
        });
        videoList.setHasFixedSize(true);
        videoList.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL, false));
    }

    private void setupViewModel(@NonNull final MovieId id) {
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(MovieDetailsViewModel.class);

        viewModel.getMovieDetails(id).observe(this, this::show);
        viewModel.getMessage().observe(this, messageResourceId ->
                snackbarPresenter.show(make(container, getString(messageResourceId), LENGTH_LONG)
                        .setAction(R.string.action_undo, v -> viewModel.toggleFavourite())
                )
        );
    }

    private void show(@Nullable final Resource<MovieDetails> resource) {
        if (resource == null) return;

        if (resource.getStatus() == ERROR)
            showError(resource.getError());
        else if (resource.getStatus() == SUCCESS && resource.getData() != null)
            bind(resource.getData());
    }

    private void showError(Throwable throwable) {
        if (throwable instanceof CommunicationException) showMessage(R.string.error_communication);
        else if (throwable instanceof AuthorizationException)
            showMessage(R.string.error_authorization);
        else showMessage(R.string.error_unknown);
    }

    private void showMessage(@StringRes int messageId) {
        snackbarPresenter.show(make(container, getString(messageId), LENGTH_LONG));
    }

    private void populateViewWith(final Movie movie) {
        bind(MovieDetails.Builder.from(movie).build());
    }

    private void bind(final MovieDetails movie) {
        final String formattedDate = getFormattedDate(movie.getReleaseDate());
        releaseDateText.setText(formattedDate);
        averageRateText.setText(String.valueOf(movie.getRating()));
        overviewText.setText(movie.getOverview());
        posterImage.setTransitionName(TransitionNameSupplier.getInstance().apply(movie));
        floatingActionButton.setImageResource(movie.isFavourite() ? R.drawable.ic_favourite : R.drawable.ic_favourite_border);

        videosAdapter.setItems(movie.getVideos());
        videoList.setAdapter(videosAdapter);

        bindReviews(movie.getReviews());

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

    private void bindReviews(@NonNull final Collection<Review> reviews) {
        reviewsNumberText.setText(getString(R.string.reviews_number, reviews.size()));

        if (!reviews.isEmpty()) {
            bindReview(getFirstReview(reviews));
            enableBrowseReviewsButtons();
        }
    }

    private void bindReview(@NonNull final Review firstReview) {
        reviewAuthor.setText(firstReview.getAuthor());
        reviewText.setText(firstReview.getContent());
    }

    private void enableBrowseReviewsButtons() {
        reviewsBrowse.setVisibility(VISIBLE);
        reviewsBrowse.setOnClickListener(v -> {
            // Do nothing
        });
    }

    private static Review getFirstReview(@NonNull Collection<Review> reviews) {
        return reviews.iterator().next();
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
