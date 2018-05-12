package pl.karollisiewicz.cinema.app.data.source.web.review;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import static java.util.Collections.emptyList;

public class Reviews implements Serializable {
    private static final long serialVersionUID = 2530555359465186111L;

    @SerializedName("results")
    private List<Review> reviews = emptyList();

    public Reviews() {
        // Empty constructor for serialization.
    }

    public Reviews(List<Review> reviews) {
        setReviews(reviews);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
