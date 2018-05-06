package pl.karollisiewicz.cinema.app.ui.movie.review;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.karollisiewicz.cinema.R;
import pl.karollisiewicz.cinema.domain.movie.review.Review;

import static java.util.Collections.emptyList;

/**
 * An {@link android.support.v7.widget.RecyclerView.Adapter} for {@link pl.karollisiewicz.cinema.domain.movie.review.Review}
 */
public final class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    @NonNull
    private List<Review> reviews = new ArrayList<>();

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setItems(@Nullable Collection<Review> reviews) {
        this.reviews = new ArrayList<>(reviews != null ? reviews : emptyList());
        notifyDataSetChanged();
    }

    final class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_author_text)
        TextView reviewAuthor;

        @BindView(R.id.review_text)
        TextView reviewText;

        ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(@NonNull final Review review) {
            reviewAuthor.setText(review.getAuthor());
            reviewText.setText(review.getContent());
        }
    }
}
