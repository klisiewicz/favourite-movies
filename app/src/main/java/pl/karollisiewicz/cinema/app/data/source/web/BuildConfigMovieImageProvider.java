package pl.karollisiewicz.cinema.app.data.source.web;

import pl.karollisiewicz.cinema.BuildConfig;

final class BuildConfigMovieImageProvider implements MovieImageProvider {
    @Override
    public String getPosterUrl(String resourceUrl) {
        return String.format("%s%s", BuildConfig.POSTER_URL, resourceUrl);
    }

    @Override
    public String getBackdropUrl(String resourceUrl) {
        return String.format("%s%s", BuildConfig.BACKDROP_URL, resourceUrl);
    }
}
