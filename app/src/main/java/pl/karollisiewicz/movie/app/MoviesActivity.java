package pl.karollisiewicz.movie.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.karollisiewicz.movie.R;
import pl.karollisiewicz.movie.app.source.MovieService;

public class MoviesActivity extends AppCompatActivity {

    @BindView(R.id.tvHello)
    TextView textHello;

    @Inject
    MovieService movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        movieService.fetchPopular()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        objects -> Log.v(MoviesActivity.class.getName(), objects.toString()),
                        throwable -> Log.v(MoviesActivity.class.getName(), "Fetching failed", throwable)
                );

        textHello.setOnClickListener(v -> startActivity(new Intent(MoviesActivity.this, MovieDetailsActivity.class)));
    }
}
