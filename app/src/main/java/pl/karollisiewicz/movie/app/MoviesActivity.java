package pl.karollisiewicz.movie.app;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.karollisiewicz.movie.R;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static pl.karollisiewicz.movie.app.Resource.Status.ERROR;
import static pl.karollisiewicz.movie.app.Resource.Status.LOADING;
import static pl.karollisiewicz.movie.app.Resource.Status.SUCCESS;

public class MoviesActivity extends AppCompatActivity {

    @BindView(R.id.container)
    ViewGroup container;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.movies_list)
    RecyclerView recyclerView;

    private MoviesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupViewModel();
    }

    private void setupRecyclerView() {
        adapter = new MoviesAdapter();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        final MoviesViewModel moviesViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(MoviesViewModel.class);

        moviesViewModel.getMovies().observe(this, resource -> {
                    if (resource.getStatus() != LOADING) progressBar.setVisibility(View.GONE);
                    if (resource.getStatus() == SUCCESS) adapter.setItems(resource.getData());
                    if (resource.getStatus() == ERROR) {
                        Snackbar.make(container, resource.getError().getMessage(), LENGTH_LONG).show();
                    }
                }
        );
    }
}
