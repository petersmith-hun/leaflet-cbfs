package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.cbfs.web.config.domain.AppInfo;
import hu.psprog.leaflet.cbfs.web.registration.impl.RegistrationTestBase;
import hu.psprog.leaflet.cbfs.web.registration.impl.conversion.AppInfoToNestedMapConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link AppInfoControllerRegistration}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AppInfoControllerRegistrationTest extends RegistrationTestBase {

    private static final String PATH_INFO = "/info";
    private static final Map<String, Map<String, String>> APP_INFO_MAP = Map.of("key", Map.of("key-value", "value"));
    private static final String JSON_RESPONSE = "{json-response}";

    @Mock
    private AppInfo appInfo;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AppInfoToNestedMapConverter appInfoToNestedMapConverter;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @InjectMocks
    private AppInfoControllerRegistration appInfoControllerRegistration;

    @Test
    public void shouldRegister() {

        // when
        appInfoControllerRegistration.register();

        // then
        assertThat(getRouteMatch(HttpMethod.get, PATH_INFO), notNullValue());
    }

    @Test
    public void shouldRouteGenerateAppInfo() throws Exception {

        // given
        given(appInfoToNestedMapConverter.convert(appInfo)).willReturn(APP_INFO_MAP);
        given(objectMapper.writeValueAsString(APP_INFO_MAP)).willReturn(JSON_RESPONSE);

        // when
        Object result = appInfoControllerRegistration.route().handle(request, response);

        // then
        assertThat(result, equalTo(JSON_RESPONSE));
    }
}
