package pl.karollisiewicz.movie.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.karollisiewicz.movie.R;
import pl.karollisiewicz.movie.domain.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String MOVIE_KEY = "MovieDetailsActivity.Movie";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.image_backdrop)
    ImageView backdropImage;

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

        final Movie movie = getMovie();
        setupActionBar(movie.getTitle());

        Picasso.with(this)
                .load(movie.getBackdropUrl())
                .into(backdropImage);
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
}
