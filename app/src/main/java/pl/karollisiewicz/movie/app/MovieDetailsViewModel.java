package pl.karollisiewicz.movie.app;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import pl.karollisiewicz.common.react.RxViewModel;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository;

public final class MovieDetailsViewModel extends RxViewModel {
    private final MovieRepository movieRepository;
    private final MutableLiveData<Resource<Movie>> movieLiveData = new MutableLiveData<>();

    @Inject
    MovieDetailsViewModel(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void addToFavourites(@NonNull Movie movie) {
        movie.favourite();
        save(movie);
    }

    public void removeFromFavourites(@NonNull Movie movie) {
        movie.unfavourite();
        save(movie);
    }

    private void save(@NonNull Movie movie) {
        add(movieRepository.save(movie)
                .subscribe(
                        savedMovie -> movieLiveData.setValue(Resource.success(savedMovie)),
                        throwable -> movieLiveData.setValue(Resource.error(throwable)))
        );
    }

    public LiveData<Resource<Movie>> getMovie() {
        return movieLiveData;
    }
}
