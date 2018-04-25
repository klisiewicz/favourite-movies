package pl.karollisiewicz.movie.app.data.source;

import org.joda.time.LocalDate;

import pl.karollisiewicz.movie.app.data.source.web.Movie;
import pl.karollisiewicz.movie.app.data.source.web.Video;

public final class MovieFactory {
    private MovieFactory() {
    }

    public static Movie aMovie() {
        final Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Title");
        movie.setOverview("Overview");
        movie.setVoteAverage(6.66);
        movie.setReleaseDate(new LocalDate(2017, 1, 27));
        movie.setFavourite(false);
        movie.setPosterPath("poster.jpg");
        movie.setBackdropPath("backdrop.jpg");
        return movie;
    }

    public static Movie favouriteMovie() {
        final Movie movie = aMovie();
        movie.setFavourite(true);
        return movie;
    }

    public static Video aVideo() {
        final Video video = new Video();
        video.setId("533ec654c3a36854480003eb");
        video.setKey("SUXWAEX2jlg");
        video.setName("Trailer 1");
        video.setSite("YouTube");
        video.setSize(720);
        video.setType("Trailer");
        return video;
    }
}
