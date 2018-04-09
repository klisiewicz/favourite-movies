package pl.karollisiewicz.movie.app.data.source;

import org.joda.time.LocalDate;

public final class MovieFactory {
    private MovieFactory() {
    }

    public static pl.karollisiewicz.movie.app.data.source.web.Movie aMovie() {
        final pl.karollisiewicz.movie.app.data.source.web.Movie movie =
                new pl.karollisiewicz.movie.app.data.source.web.Movie();
        movie.setId(1L);
        movie.setTitle("Title");
        movie.setOverview("Overview");
        movie.setVoteAverage(6.66);
        movie.setReleaseDate(new LocalDate(2017, 1, 27));
        movie.setPosterPath("poster.jpg");
        movie.setBackdropPath("backdrop.jpg");
        return movie;
    }
}
