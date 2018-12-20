package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.cbfs.web.registration.impl.RegistrationTestBase;
import hu.psprog.leaflet.failover.api.domain.StatusResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link StatusControllerRegistration}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class StatusControllerRegistrationTest extends RegistrationTestBase {

    private static final String PATH_STATUS = "/status";

    @Mock
    private FailoverStatusService failoverStatusService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @InjectMocks
    private StatusControllerRegistration  registration;

    @Test
    public void shouldRegister() {

        // when
        registration.register();

        // then
        assertThat(getRouteMatch(HttpMethod.get, PATH_STATUS), notNullValue());
    }

    @Test
    public void shouldRouteCallService() throws Exception {

        // given
        StatusResponse statusResponse = StatusResponse.getBuilder().build();
        String jsonResponse = "{status-response}";

        given(failoverStatusService.getFailoverStatus()).willReturn(statusResponse);
        given(objectMapper.writeValueAsString(statusResponse)).willReturn(jsonResponse);

        // when
        Object result = registration.route().handle(request, response);

        // then
        assertThat(result, equalTo(jsonResponse));
    }
}