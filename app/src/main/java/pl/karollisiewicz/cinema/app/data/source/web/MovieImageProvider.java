package pl.karollisiewicz.cinema.app.data.source.web;

public interface MovieImageProvider {
    String getPosterUrl(String resourceUrl);

    String getBackdropUrl(String resourceUrl);
}
