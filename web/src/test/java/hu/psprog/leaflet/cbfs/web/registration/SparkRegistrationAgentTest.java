package hu.psprog.leaflet.cbfs.web.registration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link SparkRegistrationAgent}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class SparkRegistrationAgentTest {

    private static final String REGISTRATION_1 = "Registration #1";
    private static final String REGISTRATION_2 = "Registration #2";
    private static final String REGISTRATION_3 = "Registration #3";

    private List<String> registeredMockIDs = new ArrayList<>();
    private SparkRegistrationAgent sparkRegistrationAgent;

    @Test
    public void shouldStartRegistration() {

        // given
        sparkRegistrationAgent = new SparkRegistrationAgent(prepareRegistrations());

        // when
        sparkRegistrationAgent.start();

        // then
        assertThat(registeredMockIDs.containsAll(expectedRegistrationNames()), is(true));
    }

    private class SparkRegistrationMock implements SparkRegistration {

        private final String mockID;

        private SparkRegistrationMock(String mockID) {
            this.mockID = mockID;
        }

        @Override
        public void register() {
            registeredMockIDs.add(mockID);
        }
    }

    private List<String> expectedRegistrationNames() {
        return Arrays.asList(REGISTRATION_1, REGISTRATION_2, REGISTRATION_3);
    }

    private List<SparkRegistration> prepareRegistrations() {
        return Arrays.asList(
                new SparkRegistrationMock(REGISTRATION_1),
                new SparkRegistrationMock(REGISTRATION_2),
                new SparkRegistrationMock(REGISTRATION_3));
    }
}
