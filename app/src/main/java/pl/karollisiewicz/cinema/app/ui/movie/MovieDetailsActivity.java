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
import pl.karollisiewicz.cinema.app.ui.movie.review.ReviewsAdapter;
import pl.karollisiewicz.cinema.app.ui.movie.video.VideosAdapter;
import pl.karollisiewicz.cinema.domain.exception.AuthorizationException;
import pl.karollisiewicz.cinema.domain.exception.CommunicationException;
import pl.karollisiewicz.cinema.domain.movie.Movie;
import pl.karollisiewicz.cinema.domain.movie.MovieDetails;
import pl.karollisiewicz.cinema.domain.movie.MovieId;
import pl.karollisiewicz.cinema.domain.movie.review.Review;
import pl.karollisiewicz.cinema.domain.movie.video.Video;
import pl.karollisiewicz.common.ui.Resource;
import pl.karollisiewicz.common.ui.SnackbarPresenter;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;
import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;
import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static pl.karollisiewicz.common.ui.Resource.Status.ERROR;
import static pl.karollisiewicz.common.ui.Resource.Status.SUCCESS;
import static pl.karollisiewicz.common.ui.ViewUtil.hideView;
import static pl.karollisiewicz.common.ui.ViewUtil.showView;
import static pl.karollisiewicz.common.ui.ViewUtil.showViewWhen;

public final class MovieDetailsActivity extends AppCompatActivity {
    private static final String MOVIE_KEY = "MovieDetailsActivity.MovieDetails";

    @BindView(R.id.container_layout)
    ViewGroup container;

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
    Button reviewsShowAll;

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

    @BindView(R.id.reviews_list)
    RecyclerView reviewsList;

    private ReviewsAdapter reviewsAdapter;

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
        setupVideosRecyclerView();
        setupReviewsRecyclerView();
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
        setSupportActionBar(findViewById(R.id.toolbar));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupVideosRecyclerView() {
        videosAdapter = new VideosAdapter();
        videosAdapter.setVideoClickListener(video -> {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getUrl()));
            if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
        });
        videoList.setHasFixedSize(true);
        videoList.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL, false));
    }

    private void setupReviewsRecyclerView() {
        reviewsAdapter = new ReviewsAdapter();
        reviewsList.setHasFixedSize(true);
        reviewsList.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
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

        if (resource.getStatus() == ERROR) showError(resource.getError());
        else if (resource.getStatus() == SUCCESS) bind(resource.getData());
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

    private void bind(@Nullable final MovieDetails movie) {
        if (movie == null) return;

        bindOverview(movie);
        bindVideos(movie.getVideos());
        bindReviews(movie.getReviews());
        loadImages(movie);
    }


    private void bindOverview(@NonNull MovieDetails movie) {
        final String formattedDate = getFormattedDate(movie.getReleaseDate());
        releaseDateText.setText(formattedDate);
        averageRateText.setText(String.valueOf(movie.getRating()));
        overviewText.setText(movie.getOverview());
        posterImage.setTransitionName(TransitionNameSupplier.getInstance().apply(movie));
        floatingActionButton.setImageResource(movie.isFavourite() ? R.drawable.ic_favourite : R.drawable.ic_favourite_border);
        showView(floatingActionButton);
    }

    private void bindVideos(@NonNull final Collection<Video> videos) {
        videosAdapter.setItems(videos);
        videoList.setAdapter(videosAdapter);
    }

    private String getFormattedDate(@Nullable LocalDate date) {
        return date != null ? date.toString(DateTimeFormat.longDate().withLocale(locale)) : "";
    }

    private void bindReviews(@NonNull final Collection<Review> reviews) {
        reviewsNumberText.setText(getString(R.string.reviews_number, reviews.size()));

        if (!reviews.isEmpty()) bindReview(getFirstReview(reviews));
        showViewWhen(reviewsShowAll, isMoreReviewsToShow(reviews));
        reviewsShowAll.setOnClickListener(v -> {
            showView(reviewsList);
            hideView(reviewsShowAll);
            hideView(reviewAuthor);
            hideView(reviewText);
        });

        reviewsAdapter.setItems(reviews);
        reviewsList.setAdapter(reviewsAdapter);
    }

    private void bindReview(@NonNull final Review firstReview) {
        reviewAuthor.setText(firstReview.getAuthor());
        reviewText.setText(firstReview.getContent());
    }

    private static Review getFirstReview(@NonNull Collection<Review> reviews) {
        return reviews.iterator().next();
    }

    private static boolean isMoreReviewsToShow(@NonNull Collection<Review> reviews) {
        return reviews.size() > 1;
    }

    private void loadImages(@NonNull MovieDetails movie) {
        Picasso.with(this)
                .load(movie.getBackdropUrl())
                .into(backdropImage);

        Picasso.with(this)
                .load(movie.getPosterUrl())
                .into(posterImage);
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
        hideView(floatingActionButton);
        super.onBackPressed();
    }
}
