package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.service.snapshot.impl.NonCategorizedEntryPageSnapshotRetrievalService;
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
 * Unit tests for {@link NonCategorizedEntryPageControllerRegistration}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class NonCategorizedEntryPageControllerRegistrationTest extends RegistrationTestBase {

    private static final String PATH_NON_CATEGORIZED_ENTRY_PAGE = "/entries/page/:page";
    private static final Integer PAGE_NUMBER = 4;

    @Mock
    private NonCategorizedEntryPageSnapshotRetrievalService service;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @InjectMocks
    private NonCategorizedEntryPageControllerRegistration registration;

    @Test
    public void shouldRegister() {

        // when
        registration.register();

        // then
        assertThat(getRouteMatch(HttpMethod.get, PATH_NON_CATEGORIZED_ENTRY_PAGE), notNullValue());
    }

    @Test
    public void shouldRouteCallService() throws Exception {

        // given
        EntryPageKey expectedPageKey = EntryPageKey.build(PAGE_NUMBER);
        given(request.params(PathParameter.PAGE.getParameterName())).willReturn(PAGE_NUMBER.toString());

        // when
        registration.route().handle(request, response);

        // then
        verify(service).retrieve(expectedPageKey);
    }
}
