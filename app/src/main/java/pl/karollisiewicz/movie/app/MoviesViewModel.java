package pl.karollisiewicz.movie.app;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import pl.karollisiewicz.log.Logger;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository;
import pl.karollisiewicz.movie.domain.MovieRepository.Criterion;

/**
 * ViewModel providing a list of movies to the {@link MoviesActivity}
 */
public final class MoviesViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private final Logger logger;
    private final MutableLiveData<Resource<List<Movie>>> moviesLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    MoviesViewModel(@NonNull final MovieRepository movieRepository,
                    @NonNull final Logger logger) {
        this.movieRepository = movieRepository;
        this.logger = logger;
    }

    @NonNull
    LiveData<Resource<List<Movie>>> getMovies(Criterion criterion) {
        movieRepository.fetchBy(criterion)
                .doOnSubscribe(it -> moviesLiveData.setValue(Resource.loading()))
                .doOnError(throwable -> logger.error(MoviesViewModel.class, throwable))
                .subscribe(movies -> moviesLiveData.setValue(Resource.success(movies)),
                        throwable -> moviesLiveData.setValue(Resource.error(throwable)));

        return moviesLiveData;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
