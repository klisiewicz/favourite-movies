package pl.karollisiewicz.movie.app.data;

public interface MovieImageProvider {
    String getPosterUrl(String resourceUrl);

    String getBackdropUrl(String resourceUrl);
}
