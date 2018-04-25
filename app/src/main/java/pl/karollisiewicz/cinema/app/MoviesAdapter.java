package pl.karollisiewicz.cinema.app;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.karollisiewicz.cinema.R;
import pl.karollisiewicz.cinema.app.animation.TransitionNameSupplier;
import pl.karollisiewicz.cinema.domain.Movie;

import static java.util.Collections.emptyList;

/**
 * An {@link android.support.v7.widget.RecyclerView.Adapter} for {@link Movie}
 */
public final class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<Movie> movies = new ArrayList<>();

    @Nullable
    private MovieClickListener movieClickListener;

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    void setItems(@Nullable final List<Movie> movies) {
        this.movies = (movies != null) ? new ArrayList<>(movies) : emptyList();
        notifyDataSetChanged();
    }

    void setMovieClickListener(@Nullable final MovieClickListener movieClickListener) {
        this.movieClickListener = movieClickListener;
    }

    @FunctionalInterface
    interface MovieClickListener {
        void onMovieClick(Movie movie, ImageView image);
    }

    final class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_text)
        TextView title;

        @BindView(R.id.poster_image)
        ImageView posterImage;

        MovieViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (movieClickListener != null)
                    movieClickListener.onMovieClick(movies.get(getAdapterPosition()), posterImage);
            });
        }

        void bind(@NonNull final Movie movie) {
            title.setText(movie.getTitle());
            posterImage.setTransitionName(TransitionNameSupplier.getInstance().apply(movie));

            Picasso.with(posterImage.getContext())
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.ic_videocam)
                    .error(R.drawable.ic_videocam)
                    .into(posterImage);
        }
    }
}
