package pl.karollisiewicz.movie.app.data.source.db;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import pl.karollisiewicz.movie.app.data.source.web.Movie;

public interface MovieContentValuesProvider {
    ContentValues createFrom(@NonNull Movie movie);
}
