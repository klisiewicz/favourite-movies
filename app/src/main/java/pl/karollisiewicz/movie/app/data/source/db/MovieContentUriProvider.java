package pl.karollisiewicz.movie.app.data.source.db;

import android.net.Uri;

public interface MovieContentUriProvider {
    Uri getAll();

    Uri getForId(final long id);
}
