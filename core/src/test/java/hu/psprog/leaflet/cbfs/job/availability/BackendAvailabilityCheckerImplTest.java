package hu.psprog.leaflet.cbfs.job.availability;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.cbfs.service.adapter.impl.CategoryDataAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link BackendAvailabilityCheckerImpl}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class BackendAvailabilityCheckerImplTest {

    @Mock
    private CategoryDataAdapter categoryDataAdapter;

    @InjectMocks
    private BackendAvailabilityCheckerImpl backendAvailabilityChecker;

    @Test
    public void shouldIsAvailableReturnTrue() throws CommunicationFailureException {

        // given
        given(categoryDataAdapter.retrieve(null)).willReturn(null);

        // when
        boolean result = backendAvailabilityChecker.isAvailable();

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsAvailableReturnFalse() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(categoryDataAdapter).retrieve(null);

        // when
        boolean result = backendAvailabilityChecker.isAvailable();

        // then
        assertThat(result, is(false));
    }
}