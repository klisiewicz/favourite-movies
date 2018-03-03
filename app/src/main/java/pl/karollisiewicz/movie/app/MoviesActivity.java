package pl.karollisiewicz.movie.app;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import pl.karollisiewicz.movie.R;
import pl.karollisiewicz.movie.domain.Movie;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static pl.karollisiewicz.movie.app.Resource.Status.ERROR;
import static pl.karollisiewicz.movie.app.Resource.Status.LOADING;
import static pl.karollisiewicz.movie.app.Resource.Status.SUCCESS;

public class MoviesActivity extends AppCompatActivity {

    @BindView(R.id.container_layout)
    ViewGroup container;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.movies_list)
    RecyclerView recyclerView;

    private MoviesAdapter adapter;
    private MoviesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupViewModel();
        setupRefreshLayout();
    }

    private void setupRecyclerView() {
        adapter = new MoviesAdapter();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(MoviesViewModel.class);

        viewModel.getMovies().observe(this, this::show);
    }

    private void setupRefreshLayout() {
        refreshLayout.setOnRefreshListener(() ->
                viewModel.getMovies().observe(MoviesActivity.this, MoviesActivity.this::show));
    }

    private void show(@Nullable final Resource<List<Movie>> resource) {
        if (resource == null) return;

        if (resource.getStatus() != LOADING) hideProgress();
        if (resource.getStatus() == SUCCESS) adapter.setItems(resource.getData());
        if (resource.getStatus() == ERROR) {
            Snackbar.make(container, resource.getError().getMessage(), LENGTH_LONG).show();
        }
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
    }
}
