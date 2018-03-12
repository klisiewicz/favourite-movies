package pl.karollisiewicz.movie.app.source;

public interface MovieImageProvider {
    String getPosterUrl(String resourceUrl);

    String getBackdropUrl(String resourceUrl);
}
