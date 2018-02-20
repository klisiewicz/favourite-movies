package pl.karollisiewicz.movie.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.karollisiewicz.movie.R;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository;

import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.POPULARITY;

public class MoviesActivity extends AppCompatActivity {

    @BindView(R.id.movies_list)
    ListView listView;

    @Inject
    MovieRepository movieRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        movieRepository.fetchBy(POPULARITY).observe(this, movies ->
                listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                        io.reactivex.Observable.just(movies)
                                .flatMapIterable(list -> list)
                                .map(Movie::getTitle)
                                .toList()
                                .blockingGet())
                )
        );
    }
}
