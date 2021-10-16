package hu.psprog.leaflet.cbfs.web.registration.impl.filter;

import hu.psprog.leaflet.cbfs.web.registration.impl.RegistrationTestBase;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link JSONResponseTypeFilter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class JSONResponseTypeFilterTest extends RegistrationTestBase {

    private static final String JSON_CONTENT_TYPE_FILTER = "jsonContentTypeFilter";
    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    @Mock
    private Request request;

    @Mock
    private Response response;

    @InjectMocks
    private JSONResponseTypeFilter jsonResponseTypeFilter;

    @Test
    public void shouldRegister() {

        // when
        jsonResponseTypeFilter.register();

        // then
        assertThat(getRouteMatch(HttpMethod.after, StringUtils.EMPTY), notNullValue());
    }

    @Test
    public void shouldFilterSetResponseTypeToJSON() throws Exception {

        // given
        Filter filter = getFilterExpression();

        // when
        filter.handle(request, response);

        // then
        verify(response).type(CONTENT_TYPE_APPLICATION_JSON);
    }

    private Filter getFilterExpression() {

        Method filterMethod = ReflectionUtils.findMethod(JSONResponseTypeFilter.class, JSON_CONTENT_TYPE_FILTER);
        filterMethod.setAccessible(true);

        try {
            return (Filter) filterMethod.invoke(jsonResponseTypeFilter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to invoke jsonContentTypeFilter method.", e);
        }
    }
}
