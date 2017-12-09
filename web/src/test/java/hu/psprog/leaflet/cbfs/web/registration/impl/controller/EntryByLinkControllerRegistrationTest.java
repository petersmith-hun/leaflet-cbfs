package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.service.snapshot.impl.EntrySnapshotRetrievalService;
import hu.psprog.leaflet.cbfs.web.registration.impl.RegistrationTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EntryByLinkControllerRegistration}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryByLinkControllerRegistrationTest extends RegistrationTestBase {

    private static final String PATH_ENTRIES_BY_LINK = "/entries/link/:link";
    private static final String TEST_LINK_1 = "test-link-1";

    @Mock
    private EntrySnapshotRetrievalService service;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @InjectMocks
    private EntryByLinkControllerRegistration registration;

    @Test
    public void shouldRegister() {

        // when
        registration.register();

        // then
        assertThat(getRouteMatch(HttpMethod.get, PATH_ENTRIES_BY_LINK), notNullValue());
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
