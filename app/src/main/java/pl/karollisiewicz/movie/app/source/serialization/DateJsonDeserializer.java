package pl.karollisiewicz.movie.app.source.serialization;

import android.support.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Deserializer for {@link java.util.Date} objects
 */
public final class DateJsonDeserializer implements JsonDeserializer<Date> {
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    @Nullable
    public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) {
        try {
            return dateFormat.parse(json.getAsString());
        } catch (ParseException e) {
            return null;
        }
    }
}
