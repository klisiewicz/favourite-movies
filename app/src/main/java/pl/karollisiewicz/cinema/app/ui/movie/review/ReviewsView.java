package pl.karollisiewicz.cinema.app.ui.movie.review;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.karollisiewicz.cinema.R;
import pl.karollisiewicz.cinema.domain.movie.review.Review;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static pl.karollisiewicz.common.ui.ViewUtil.hideView;
import static pl.karollisiewicz.common.ui.ViewUtil.showView;
import static pl.karollisiewicz.common.ui.ViewUtil.showViewWhen;

public final class ReviewsView extends ConstraintLayout {

    @BindView(R.id.reviews_number_text)
    TextView reviewsNumberText;

    @BindView(R.id.review_author_text)
    TextView reviewAuthor;

    @BindView(R.id.review_text)
    TextView reviewText;

    @BindView(R.id.browse_reviews_button)
    Button reviewsShowAll;

    @BindView(R.id.reviews_list)
    RecyclerView reviewsList;

    private ReviewsAdapter reviewsAdapter;

    public ReviewsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_movie_reviews, this);
        ButterKnife.bind(this);

        reviewsAdapter = new ReviewsAdapter();
        reviewsList.setHasFixedSize(true);
        reviewsList.setLayoutManager(new LinearLayoutManager(context, VERTICAL, false));
    }


    public void bind(@NonNull final Collection<Review> reviews) {
        reviewsNumberText.setText(getContext().getString(R.string.reviews_number, reviews.size()));

        if (!reviews.isEmpty()) bindReview(getFirstReview(reviews));
        showViewWhen(reviewsShowAll, isMoreReviewsToShow(reviews));
        reviewsShowAll.setOnClickListener(v -> {
            showView(reviewsList);
            hideView(reviewsShowAll);
            hideView(reviewAuthor);
            hideView(reviewText);
        });

        reviewsAdapter.setItems(reviews);
        reviewsList.setAdapter(reviewsAdapter);
    }

    private void bindReview(@NonNull final Review firstReview) {
        reviewAuthor.setText(firstReview.getAuthor());
        reviewText.setText(firstReview.getContent());
    }

    private static Review getFirstReview(@NonNull Collection<Review> reviews) {
        return reviews.iterator().next();
    }

    private static boolean isMoreReviewsToShow(@NonNull Collection<Review> reviews) {
        return reviews.size() > 1;
    }
}
