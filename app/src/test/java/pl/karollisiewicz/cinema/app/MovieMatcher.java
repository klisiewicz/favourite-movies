package pl.karollisiewicz.cinema.app;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.joda.time.LocalDate;

import pl.karollisiewicz.cinema.domain.movie.Movie;
import pl.karollisiewicz.cinema.domain.movie.MovieDetails;

import static org.hamcrest.CoreMatchers.equalTo;

public final class MovieMatcher {
    private MovieMatcher() {
        // No instances should be created
    }

    public static Matcher<Movie> withId(final String id) {
        return new FeatureMatcher<Movie, String>(equalTo(id), "id", "id") {
            @Override
            protected String featureValueOf(final Movie actual) {
                return actual.getId().getValue();
            }
        };
    }

    public static Matcher<Movie> isTitled(final String title) {
        return new FeatureMatcher<Movie, String>(equalTo(title), "title", "title") {
            @Override
            protected String featureValueOf(final Movie actual) {
                return actual.getTitle();
            }
        };
    }

    public static Matcher<Movie> isRated(final double rating) {
        return new FeatureMatcher<Movie, Double>(equalTo(rating), "rating", "rating") {
            @Override
            protected Double featureValueOf(final Movie actual) {
                return actual.getRating();
            }
        };
    }

    public static Matcher<Movie> wasReleasedOn(final LocalDate date) {
        return new FeatureMatcher<Movie, LocalDate>(equalTo(date), "releaseDate", "releaseDate") {
            @Override
            protected LocalDate featureValueOf(final Movie actual) {
                return actual.getReleaseDate();
            }
        };
    }

    public static Matcher<Movie> hasPosterUrl(final String posterUrl) {
        return new FeatureMatcher<Movie, String>(equalTo(posterUrl), "posterUrl", "posterUrl") {
            @Override
            protected String featureValueOf(final Movie actual) {
                return actual.getPosterUrl();
            }
        };
    }

    public static Matcher<Movie> hasBackDropUrl(final String backdropUrl) {
        return new FeatureMatcher<Movie, String>(equalTo(backdropUrl), "backdropUrl", "backdropUrl") {
            @Override
            protected String featureValueOf(final Movie actual) {
                return actual.getBackdropUrl();
            }
        };
    }

    public static Matcher<MovieDetails> favourite() {
        return new FeatureMatcher<MovieDetails, Boolean>(equalTo(true), "favourite", "favourite") {
            @Override
            protected Boolean featureValueOf(final MovieDetails actual) {
                return actual.isFavourite();
            }
        };
    }

    public static Matcher<MovieDetails> hasOverview(final String overview) {
        return new FeatureMatcher<MovieDetails, String>(equalTo(overview), "overview", "overview") {
            @Override
            protected String featureValueOf(final MovieDetails actual) {
                return actual.getOverview();
            }
        };
    }
}
