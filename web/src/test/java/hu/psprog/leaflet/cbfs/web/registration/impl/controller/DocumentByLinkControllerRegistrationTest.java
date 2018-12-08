package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.service.snapshot.impl.DocumentSnapshotRetrievalService;
import hu.psprog.leaflet.cbfs.web.registration.impl.RegistrationTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DocumentByLinkControllerRegistration}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentByLinkControllerRegistrationTest extends RegistrationTestBase {

    private static final String PATH_DOCUMENTS_BY_LINK = "/documents/link/:link";
    private static final String TEST_LINK_1 = "test-link-1";

    @Mock
    private DocumentSnapshotRetrievalService service;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @InjectMocks
    private DocumentByLinkControllerRegistration registration;

    @Test
    public void shouldRegister() {

        // when
        registration.register();

        // then
        assertThat(getRouteMatch(HttpMethod.get, PATH_DOCUMENTS_BY_LINK), notNullValue());
    }

    @Test
    public void shouldRouteCallService() throws Exception {

        // given
        given(request.params(PathParameter.LINK.getParameterName())).willReturn(TEST_LINK_1);

        // when
        registration.route().handle(request, response);

        // then
        verify(service).retrieve(TEST_LINK_1);
    }
}
