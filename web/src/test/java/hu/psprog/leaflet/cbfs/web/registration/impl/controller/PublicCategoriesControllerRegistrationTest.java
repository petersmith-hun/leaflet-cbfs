package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.service.snapshot.impl.CategoryListSnapshotRetrievalService;
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
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link PublicCategoriesControllerRegistration}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class PublicCategoriesControllerRegistrationTest extends RegistrationTestBase {

    private static final String PATH_CATEGORIES_PUBLIC = "/categories/public";

    @Mock
    private CategoryListSnapshotRetrievalService service;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @InjectMocks
    private PublicCategoriesControllerRegistration registration;

    @Test
    public void shouldRegister() {

        // when
        registration.register();

        // then
        assertThat(getRouteMatch(HttpMethod.get, PATH_CATEGORIES_PUBLIC), notNullValue());
    }

    @Test
    public void shouldRouteCallService() throws Exception {

        // when
        registration.route().handle(request, response);

        // then
        verify(service).retrieve(null);
    }
}
