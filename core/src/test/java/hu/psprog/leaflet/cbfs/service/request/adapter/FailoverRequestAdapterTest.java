package hu.psprog.leaflet.cbfs.service.request.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link FailoverRequestAdapter}.
 *
 * @author Peter Smith
 */
public class FailoverRequestAdapterTest {

    private static final String COMMON_ID = "common-device-and-client-id";

    private FailoverRequestAdapter failoverRequestAdapter;

    @BeforeEach
    public void setup() {
        failoverRequestAdapter = new FailoverRequestAdapter(COMMON_ID);
    }

    @Test
    public void shouldProvideDeviceID() {

        // when
        String result = failoverRequestAdapter.provideDeviceID();

        // then
        assertThat(result, equalTo(COMMON_ID));
    }

    @Test
    public void shouldProvideClientID() {

        // when
        String result = failoverRequestAdapter.provideClientID();

        // then
        assertThat(result, equalTo(COMMON_ID));
    }
}