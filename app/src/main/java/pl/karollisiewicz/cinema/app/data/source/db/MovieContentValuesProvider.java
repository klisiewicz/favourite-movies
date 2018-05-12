package pl.karollisiewicz.cinema.app.data.source.db;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import pl.karollisiewicz.cinema.app.data.source.web.Movie;

public interface MovieContentValuesProvider {
    ContentValues createFrom(@NonNull Movie movie);
}
