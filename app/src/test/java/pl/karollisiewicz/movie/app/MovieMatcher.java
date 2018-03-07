package pl.karollisiewicz.movie.app;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.Date;

import pl.karollisiewicz.movie.domain.Movie;

import static org.hamcrest.CoreMatchers.equalTo;

public final class MovieMatcher {
    private MovieMatcher() {
        // No instances should be created
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

    public static Matcher<Movie> wasReleasedOn(final Date date) {
        return new FeatureMatcher<Movie, Date>(equalTo(date), "releaseDate", "releaseDate") {
            @Override
            protected Date featureValueOf(final Movie actual) {
                return actual.getReleaseDate();
            }
        };
    }

    public static Matcher<Movie> hasOverview(final String overview) {
        return new FeatureMatcher<Movie, String>(equalTo(overview), "overview", "overview") {
            @Override
            protected String featureValueOf(final Movie actual) {
                return actual.getOverview();
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
}
