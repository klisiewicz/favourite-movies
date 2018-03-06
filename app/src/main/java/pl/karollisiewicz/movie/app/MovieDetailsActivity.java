package pl.karollisiewicz.movie.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import pl.karollisiewicz.movie.R;
import pl.karollisiewicz.movie.domain.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String MOVIE_KEY = "pl.karollisiewicz.movie-details";

    public static void start(@NonNull final Context context, @NonNull final Movie movie) {
        final Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_KEY, movie);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setupActionBar(getMovieTitle());
    }

    private String getMovieTitle() {
        final Intent intent = getIntent();
        if (intent == null) return "";

        final Movie movie = (Movie) intent.getSerializableExtra(MOVIE_KEY);
        return movie.getTitle();
    }

    private void setupActionBar(final String title) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
