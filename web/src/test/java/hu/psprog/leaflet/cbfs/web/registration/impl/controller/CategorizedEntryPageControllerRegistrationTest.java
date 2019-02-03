package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.service.snapshot.impl.CategorizedEntryPageSnapshotRetrievalService;
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
 * Unit tests for {@link CategorizedEntryPageControllerRegistration}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategorizedEntryPageControllerRegistrationTest extends RegistrationTestBase {

    private static final String PATH_CATEGORIZED_ENTRY_PAGE = "/entries/category/:categoryID/page/:page";
    private static final Integer PAGE_NUMBER = 2;
    private static final Long CATEGORY_ID = 3L;

    @Mock
    private CategorizedEntryPageSnapshotRetrievalService service;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @InjectMocks
    private CategorizedEntryPageControllerRegistration registration;

    @Test
    public void shouldRegister() {

        // when
        registration.register();

        // then
        assertThat(getRouteMatch(HttpMethod.get, PATH_CATEGORIZED_ENTRY_PAGE), notNullValue());
    }

    @Test
    public void shouldRouteCallService() throws Exception {

        // given
        EntryPageKey expectedPageKey = EntryPageKey.build(PAGE_NUMBER, CATEGORY_ID);
        given(request.params(PathParameter.PAGE.getParameterName())).willReturn(PAGE_NUMBER.toString());
        given(request.params(PathParameter.CATEGORY_ID.getParameterName())).willReturn(CATEGORY_ID.toString());

        // when
        registration.route().handle(request, response);

        // then
        verify(service).retrieve(expectedPageKey);
    }
}
