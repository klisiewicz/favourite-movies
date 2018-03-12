package pl.karollisiewicz.movie.app.source.serialization;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import pl.karollisiewicz.log.Logger;

/**
 * Deserializer for {@link java.util.Date} objects
 */
public final class DateJsonDeserializer implements JsonDeserializer<Date> {
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final Logger logger;

    @Inject
    public DateJsonDeserializer(@NonNull final Logger logger) {
        this.logger = logger;
    }

    @Override
    @Nullable
    public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) {
        try {
            return dateFormat.parse(json.getAsString());
        } catch (Exception e) {
            logger.error(DateJsonDeserializer.class, e);
            return null;
        }
    }
}
