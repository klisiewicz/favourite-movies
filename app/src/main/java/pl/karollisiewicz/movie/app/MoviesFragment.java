package pl.karollisiewicz.movie.app;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import pl.karollisiewicz.movie.R;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository.Criterion;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static pl.karollisiewicz.movie.app.Resource.Status.ERROR;
import static pl.karollisiewicz.movie.app.Resource.Status.LOADING;
import static pl.karollisiewicz.movie.app.Resource.Status.SUCCESS;
import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.POPULARITY;
import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.RATHING;

public class MoviesFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "MoviesFragment.Type";

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

    private Criterion criterion;

    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newPopularInstance() {
        return newInstance(POPULARITY);
    }

    public static MoviesFragment newTopRatedInstance() {
        return newInstance(RATHING);
    }

    private static MoviesFragment newInstance(@NonNull final Criterion criterion) {
        final MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(createBundle(criterion));
        return fragment;
    }

    @NonNull
    private static Bundle createBundle(@NonNull Criterion criterion) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SECTION_NUMBER, criterion);
        return args;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);

        final Bundle args = getArguments();
        criterion = (Criterion) args.getSerializable(ARG_SECTION_NUMBER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupRecyclerView();
        setupViewModel();
        setupRefreshLayout();
    }

    private void setupRecyclerView() {
        adapter = new MoviesAdapter();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(MoviesViewModel.class);

        viewModel.getMovies(criterion).observe(this, this::show);
    }

    private void setupRefreshLayout() {
        refreshLayout.setOnRefreshListener(() ->
                viewModel.getMovies(criterion).observe(MoviesFragment.this, MoviesFragment.this::show));
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
