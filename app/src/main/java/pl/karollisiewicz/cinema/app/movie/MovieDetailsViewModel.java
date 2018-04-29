package pl.karollisiewicz.cinema.app.movie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import pl.karollisiewicz.cinema.R;
import pl.karollisiewicz.cinema.domain.movie.MovieDetails;
import pl.karollisiewicz.cinema.domain.movie.MovieId;
import pl.karollisiewicz.cinema.domain.movie.MovieRepository;
import pl.karollisiewicz.common.react.RxViewModel;
import pl.karollisiewicz.common.ui.Resource;

import static pl.karollisiewicz.common.ui.Resource.error;
import static pl.karollisiewicz.common.ui.Resource.success;

public final class MovieDetailsViewModel extends RxViewModel {
    private final MovieRepository movieRepository;
    private final MutableLiveData<Resource<MovieDetails>> movieLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> messageLiveData = new MutableLiveData<>();

    @Inject
    MovieDetailsViewModel(@NonNull final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void toggleFavourite() {
        if (movieLiveData.getValue() == null || movieLiveData.getValue().getData() == null) return;

        final MovieDetails movie = movieLiveData.getValue().getData();
        if (movie.isFavourite()) movie.unfavourite();
        else movie.favourite();

        save(movie);
    }

    private void save(@NonNull MovieDetails movie) {
        add(movieRepository.save(movie)
                .subscribe(
                        savedMovie -> {
                            movieLiveData.setValue(success(savedMovie));
                            messageLiveData.setValue(savedMovie.isFavourite() ? R.string.favourite_added : R.string.favourite_removed);
                        },
                        throwable -> movieLiveData.setValue(error(throwable))
                )
        );
    }

    public LiveData<Resource<MovieDetails>> getMovieDetails(final MovieId id) {
        fetchMovieDetailsWhenNeeded(id);
        return movieLiveData;
    }

    private void fetchMovieDetailsWhenNeeded(MovieId id) {
        if (movieLiveData.getValue() == null)
            fetchMovieDetails(id);
    }

    private void fetchMovieDetails(MovieId id) {
        add(movieRepository.fetchBy(id)
            .subscribe(
                    movie -> movieLiveData.setValue(success(movie)),
                    throwable -> movieLiveData.setValue(error(throwable))
            )
        );
    }

    public MutableLiveData<Integer> getMessage() {
        return messageLiveData;
    }
}
