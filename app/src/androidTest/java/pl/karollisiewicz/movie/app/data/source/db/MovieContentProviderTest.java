package pl.karollisiewicz.movie.app.data.source.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class MovieContentProviderTest {

    private Context context;

    @Before
    public void beforeEach() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void test() {
        assertThat(true, is(true));
    }
}
