package pl.karollisiewicz.cinema.app.data.source;

import org.joda.time.LocalDate;

import pl.karollisiewicz.cinema.app.data.source.web.Movie;
import pl.karollisiewicz.cinema.app.data.source.web.review.Review;
import pl.karollisiewicz.cinema.app.data.source.web.video.Video;

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

    public static Review aReview() {
        final Review review = new Review();
        review.setAuthor("Frank Ochieng");
        review.setContent("Summertime 2016 has not been very kind to DC Comics-based personalities " +
                "looking to shine consistently like their big screen Marvel Comics counterparts.");
        review.setUrl("https://www.themoviedb.org/review/57a814dc9251415cfb00309a");
        return review;
    }
}
