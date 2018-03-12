package pl.karollisiewicz.movie.app;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.karollisiewicz.movie.R;
import pl.karollisiewicz.movie.domain.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String MOVIE_KEY = "MovieDetailsActivity.Movie";

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
    Locale locale;

    public static void start(@NonNull final Context context, @NonNull final Movie movie) {
        final Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_KEY, movie);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        AndroidInjection.inject(this);

        final Movie movie = getMovie();
        setupActionBar(movie.getTitle());
        populateViewWith(movie);

        floatingActionButton.setOnClickListener(v -> {
            // Phase 2 - Add to favourites.
        });
    }

    private Movie getMovie() {
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
        Picasso.with(this)
                .load(movie.getBackdropUrl())
                .into(backdropImage);

        Picasso.with(this)
                .load(movie.getPosterUrl())
                .into(posterImage);

        final String formattedDate = getFormattedDate(movie.getReleaseDate());
        releaseDateText.setText(formattedDate);
        averageRateText.setText(String.valueOf(movie.getRating()));
        overviewText.setText(movie.getOverview());
    }

    private String getFormattedDate(@Nullable Date date) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, locale);
        return date != null ? dateFormat.format(date) : "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
