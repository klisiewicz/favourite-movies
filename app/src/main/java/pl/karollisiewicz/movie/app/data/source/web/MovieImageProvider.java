package pl.karollisiewicz.movie.app.data.source.web;

public interface MovieImageProvider {
    String getPosterUrl(String resourceUrl);

    String getBackdropUrl(String resourceUrl);
}
