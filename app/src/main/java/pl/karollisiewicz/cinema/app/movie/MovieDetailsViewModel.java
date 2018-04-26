package pl.karollisiewicz.cinema.app.movie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import pl.karollisiewicz.cinema.R;
import pl.karollisiewicz.cinema.domain.movie.Movie;
import pl.karollisiewicz.cinema.domain.movie.MovieRepository;
import pl.karollisiewicz.common.react.RxViewModel;
import pl.karollisiewicz.common.ui.Resource;

public final class MovieDetailsViewModel extends RxViewModel {
    private final MovieRepository movieRepository;
    private final MutableLiveData<Resource<Movie>> movieLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> messageLiveData = new MutableLiveData<>();

    @Inject
    MovieDetailsViewModel(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void toggleFavourite(@NonNull Movie movie) {
        if (movie.isFavourite()) movie.unfavourite();
        else movie.favourite();

        save(movie);
    }

    private void save(@NonNull Movie movie) {
        add(movieRepository.save(movie)
                .subscribe(
                        savedMovie -> {
                            movieLiveData.setValue(Resource.success(savedMovie));
                            messageLiveData.setValue(savedMovie.isFavourite() ? R.string.favourite_added : R.string.favourite_removed);
                        },
                        throwable -> movieLiveData.setValue(Resource.error(throwable)))
        );
    }

    public LiveData<Resource<Movie>> getMovie() {
        return movieLiveData;
    }

    public MutableLiveData<Integer> getMessage() {
        return messageLiveData;
    }
}
