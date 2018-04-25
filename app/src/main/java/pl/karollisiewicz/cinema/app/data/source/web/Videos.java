package pl.karollisiewicz.cinema.app.data.source.web;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import static java.util.Collections.emptyList;

public class Videos implements Serializable {
    private static final long serialVersionUID = 8106720053479273329L;

    @SerializedName("results")
    private List<Video> videos = emptyList();

    public Videos() {
        // Empty constructor for serialization.
    }

    public Videos(List<Video> videos) {
        setVideos(videos);
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(@NonNull final List<Video> videos) {
        this.videos = videos;
    }
}
