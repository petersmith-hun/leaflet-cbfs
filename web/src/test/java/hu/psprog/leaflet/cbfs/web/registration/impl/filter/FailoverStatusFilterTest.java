package hu.psprog.leaflet.cbfs.web.registration.impl.filter;

import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.cbfs.web.registration.impl.RegistrationTestBase;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.ReflectionUtils;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link FailoverStatusFilter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FailoverStatusFilterTest extends RegistrationTestBase {

    private static final String STATUS_FILTER = "statusFilter";

    @Mock
    private FailoverStatusService failoverStatusService;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @InjectMocks
    private FailoverStatusFilter failoverStatusFilter;

    @Test
    public void shouldRegister() {

        // when
        failoverStatusFilter.register();

        // then
        assertThat(getRouteMatch(HttpMethod.after, StringUtils.EMPTY), notNullValue());
    }

    @Test
    public void shouldFilterCallFailoverStatusService() throws Exception {

        // given
        given(request.pathInfo()).willReturn("/documents/public");

        // when
        getFilterExpression().handle(request, response);

        // then
        verify(failoverStatusService).trafficReceived();
    }

    @Test
    public void shouldFilterNotCallFailoverStatusServiceForStatusEndpoint() throws Exception {

        // given
        given(request.pathInfo()).willReturn("/status");

        // when
        getFilterExpression().handle(request, response);

        // then
        verify(failoverStatusService, never()).trafficReceived();
    }

    @Test
    public void shouldFilterNotCallFailoverStatusServiceForInfoEndpoint() throws Exception {

        // given
        given(request.pathInfo()).willReturn("/info");

        // when
        getFilterExpression().handle(request, response);

        // then
        verify(failoverStatusService, never()).trafficReceived();
    }

    private Filter getFilterExpression() {

        Method filterMethod = ReflectionUtils.findMethod(FailoverStatusFilter.class, STATUS_FILTER);
        filterMethod.setAccessible(true);

        try {
            return (Filter) filterMethod.invoke(failoverStatusFilter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to invoke jsonContentTypeFilter method.", e);
        }
    }
}