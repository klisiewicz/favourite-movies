package pl.karollisiewicz.movie.app;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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

public class MoviesActivity extends AppCompatActivity {

    @BindView(R.id.movies_list)
    ListView listView;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        final MoviesViewModel moviesViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(MoviesViewModel.class);
        moviesViewModel.getMovies().observe(this, resource ->
                listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                        io.reactivex.Observable.just(resource)
                                .filter(Resource::isData)
                                .map(Resource::getData)
                                .flatMapIterable(list -> list)
                                .map(Movie::getTitle)
                                .toList()
                                .blockingGet())
                )
        );
    }
}
