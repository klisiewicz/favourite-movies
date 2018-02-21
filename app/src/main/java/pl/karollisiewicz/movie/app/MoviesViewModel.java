package pl.karollisiewicz.movie.app;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import pl.karollisiewicz.movie.app.react.Schedulers;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository;

import static pl.karollisiewicz.movie.domain.MovieRepository.Criterion.POPULARITY;

/**
 * ViewModel providing a list of movies to the {@link MoviesActivity}
 */
public final class MoviesViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private final Schedulers schedulers;
    private final MutableLiveData<Resource<List<Movie>>> moviesLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MoviesViewModel(@NonNull final MovieRepository movieRepository,
                           @NonNull final Schedulers schedulers) {
        this.movieRepository = movieRepository;
        this.schedulers = schedulers;

        movieRepository.fetchBy(POPULARITY)
                .subscribeOn(schedulers.getSubscriber())
                .observeOn(schedulers.getObserver())
                .doOnSubscribe(it -> moviesLiveData.setValue(Resource.loading()))
                .subscribe(movies -> moviesLiveData.setValue(Resource.success(movies)),
                        throwable -> moviesLiveData.setValue(Resource.error(throwable)))
        ;
    }

    @NonNull
    MutableLiveData<Resource<List<Movie>>> getMovies() {
        return moviesLiveData;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
