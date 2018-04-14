package pl.karollisiewicz.movie.app;

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
import pl.karollisiewicz.movie.R;
import pl.karollisiewicz.movie.app.animation.TransitionNameSupplier;
import pl.karollisiewicz.movie.domain.Movie;

import static java.util.Collections.emptyList;

/**
 * An {@link android.support.v7.widget.RecyclerView.Adapter} for {@link pl.karollisiewicz.movie.domain.Movie}
 */
public final class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<Movie> movies = new ArrayList<>();

    @Nullable
    private MovieClickListener movieClickListener;

    @Nullable
    private FavouriteMovieClickListener favouriteMovieClickListener;

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

    public void setFavouriteMovieClickListener(@Nullable FavouriteMovieClickListener favouriteMovieClickListener) {
        this.favouriteMovieClickListener = favouriteMovieClickListener;
    }

    @FunctionalInterface
    interface MovieClickListener {
        void onMovieClick(Movie movie, ImageView image);
    }

    @FunctionalInterface
    interface FavouriteMovieClickListener {
        void onFavouriteClick(Movie movie);
    }

    final class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_text)
        TextView title;

        @BindView(R.id.poster_image)
        ImageView posterImage;

        @BindView(R.id.favourite_image)
        ImageView favouriteImage;

        MovieViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (movieClickListener != null)
                    movieClickListener.onMovieClick(movies.get(getAdapterPosition()), posterImage);
            });

            favouriteImage.setOnClickListener(v -> {
                if (favouriteMovieClickListener != null)
                    favouriteMovieClickListener.onFavouriteClick(movies.get(getAdapterPosition()));
            });
        }

        void bind(@NonNull final Movie movie) {
            title.setText(movie.getTitle());
            posterImage.setTransitionName(TransitionNameSupplier.getInstance().apply(movie));
            favouriteImage.setImageResource(movie.isFavourite() ? R.drawable.ic_favourite : R.drawable.ic_favourite_border);

            Picasso.with(posterImage.getContext())
                    .load(movie.getPosterUrl())
                    .into(posterImage);
        }
    }
}
