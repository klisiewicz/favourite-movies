package pl.karollisiewicz.movie.app.source;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.karollisiewicz.movie.domain.Movie;
import pl.karollisiewicz.movie.domain.MovieRepository;

/**
 * Repository that utilizes {@link MovieService} web service to fetch movies.
 */
public final class MovieWebRepository implements MovieRepository {
    private final MutableLiveData<Iterable<Movie>> movieLiveData = new MutableLiveData<>();
    private final MovieService movieService;

    @Inject
    public MovieWebRepository(@NonNull final MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public LiveData<Iterable<Movie>> fetchBy(@NonNull Criterion criterion) {
        movieService.fetchPopular()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies ->
                                movieLiveData.setValue(Observable.just(movies.getMovies())
                                        .flatMapIterable(list -> list)
                                        .map(it -> new Movie.Builder()
                                                .setTitle(it.getTitle())
                                                .setOverview(it.getOverview())
                                                .setRating(it.getVoteAverage())
                                                .setReleaseDate(null)
                                                .setImageUrl("http://image.tmdb.org/t/p/w185/" + it.getPosterPath())
                                                .build()
                                        )
                                        .toList()
                                        .blockingGet())
                        ,
                        throwable -> Log.v(MovieWebRepository.class.getName(), "Fetching failed", throwable)
                );

        return movieLiveData;
    }
}
