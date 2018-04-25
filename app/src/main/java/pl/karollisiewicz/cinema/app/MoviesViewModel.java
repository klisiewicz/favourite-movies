package pl.karollisiewicz.cinema.app;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import pl.karollisiewicz.cinema.domain.Movie;
import pl.karollisiewicz.cinema.domain.MovieRepository;
import pl.karollisiewicz.common.react.RxViewModel;
import pl.karollisiewicz.common.ui.Resource;

/**
 * ViewModel providing a list of movies to the {@link MoviesActivity}
 */
public final class MoviesViewModel extends RxViewModel {
    private final MovieRepository movieRepository;
    private final MutableLiveData<Resource<List<Movie>>> moviesLiveData = new MutableLiveData<>();

    @Inject
    MoviesViewModel(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    @NonNull
    LiveData<Resource<List<Movie>>> getMovies(MovieRepository.Criterion criterion) {
        add(movieRepository.fetchBy(criterion)
                .doOnSubscribe(it -> moviesLiveData.setValue(Resource.loading()))
                .subscribe(
                        movies -> moviesLiveData.setValue(Resource.success(movies)),
                        throwable -> moviesLiveData.setValue(Resource.error(throwable))
                )
        );

        return moviesLiveData;
    }
}
