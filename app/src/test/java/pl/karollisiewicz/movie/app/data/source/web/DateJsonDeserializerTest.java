package pl.karollisiewicz.movie.app.data.source.web;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import pl.karollisiewicz.movie.app.ConsoleLogger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class DateJsonDeserializerTest {
    private DateJsonDeserializer objectUnderTest;

    @Before
    public void beforeEach() {
        objectUnderTest = new DateJsonDeserializer(new ConsoleLogger());
    }

    @Test
    public void whenEmptyStringIsPassed_NullIsReturned() {
        // Given
        final JsonElement jsonDate = new JsonPrimitive("");

        // When:
        final org.joda.time.LocalDate date = objectUnderTest.deserialize(jsonDate, null, null);

        // Then
        assertThat(date, is(nullValue()));
    }

    @Test
    public void whenDateFormatIsInvalid_NullIsReturned() {
        // Given
        final JsonElement jsonDate = new JsonPrimitive("17.03.2017");

        // When:
        final LocalDate date = objectUnderTest.deserialize(jsonDate, null, null);

        // Then
        assertThat(date, is(nullValue()));
    }

    @Test
    public void whenDateFormatIsValid_DateIsReturned() {
        // Given
        final JsonElement jsonDate = new JsonPrimitive("2017-03-17");

        // When:
        final LocalDate date = objectUnderTest.deserialize(jsonDate, null, null);

        // Then:
        assertThat(date, is(not(nullValue())));
        assertThat(date.getYear(), is(2017));
        assertThat(date.getMonthOfYear(), is(3));
        assertThat(date.getDayOfMonth(), is(17));
    }
}
