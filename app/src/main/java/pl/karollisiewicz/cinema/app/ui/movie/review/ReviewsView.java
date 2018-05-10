package pl.karollisiewicz.cinema.app.ui.movie.review;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.karollisiewicz.cinema.R;
import pl.karollisiewicz.cinema.domain.movie.review.Review;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public final class ReviewsView extends ConstraintLayout {
    @BindView(R.id.reviews_number_text)
    TextView reviewsNumberText;

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

    public void setReviewClickListener(ReviewClickListener reviewClickListener) {
        reviewsAdapter.setReviewClickListener(reviewClickListener);
    }

    public void bind(@NonNull final Collection<Review> reviews) {
        reviewsNumberText.setText(getContext().getString(R.string.reviews_number, reviews.size()));
        reviewsAdapter.setItems(reviews);
        reviewsList.setAdapter(reviewsAdapter);
    }

    @FunctionalInterface
    public interface ReviewClickListener {
        void onReviewClick(@NonNull Review review);
    }
}
