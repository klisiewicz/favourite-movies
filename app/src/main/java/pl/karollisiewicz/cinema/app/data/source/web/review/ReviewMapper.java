package pl.karollisiewicz.cinema.app.data.source.web.review;

import android.support.annotation.NonNull;

public final class ReviewMapper {
    private ReviewMapper() {
    }

    @NonNull
    public static pl.karollisiewicz.cinema.domain.movie.review.Review toDomain(@NonNull final Review review) {
        return new pl.karollisiewicz.cinema.domain.movie.review.Review.Builder()
                .setAuthor(review.getAuthor())
                .setContent(review.getContent())
                .setUrl(review.getUrl())
                .build();
    }
}
