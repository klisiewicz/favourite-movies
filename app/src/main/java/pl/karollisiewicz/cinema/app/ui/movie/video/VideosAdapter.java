package pl.karollisiewicz.cinema.app.ui.movie.video;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.karollisiewicz.cinema.R;
import pl.karollisiewicz.cinema.domain.movie.video.Video;

import static java.util.Collections.emptyList;

/**
 * An {@link android.support.v7.widget.RecyclerView.Adapter} for {@link Video}
 */
public final class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
    @NonNull
    private List<Video> videos = new ArrayList<>();

    @Nullable
    private VideoClickListener videoClickListener;

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void setItems(@Nullable Collection<Video> videos) {
        this.videos = new ArrayList<>(videos != null ? videos : emptyList());
        notifyDataSetChanged();
    }

    public void setVideoClickListener(@Nullable final VideoClickListener videoClickListener) {
        this.videoClickListener = videoClickListener;
    }

    @FunctionalInterface
    public interface VideoClickListener {
        void onVideoClick(@NonNull Video video);
    }

    final class VideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.video_thumbnail)
        ImageView thumbnail;

        @BindView(R.id.video_play)
        ImageButton play;

        VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            play.setOnClickListener(v -> {
                if (videoClickListener != null)
                    videoClickListener.onVideoClick(videos.get(getAdapterPosition()));
            });
        }

        void bind(@NonNull final Video video) {

            Picasso.with(thumbnail.getContext())
                    .load(video.getThumbnailUrl())
                    .placeholder(R.drawable.ic_camera)
                    .error(R.drawable.ic_camera)
                    .into(thumbnail);
        }
    }
}
