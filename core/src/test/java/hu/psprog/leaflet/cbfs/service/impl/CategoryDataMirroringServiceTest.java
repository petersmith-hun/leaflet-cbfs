package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.cbfs.domain.MirrorType;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.cbfs.service.adapter.impl.CategoryDataAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CategoryDataMirroringService}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryDataMirroringServiceTest {

    private static final WrapperBodyDataModel<CategoryListDataModel> CATEGORY_LIST = WrapperBodyDataModel.getBuilder().build();

    @Mock
    private CategoryDataAdapter categoryDataAdapter;

    @Mock
    private FailoverStatusService failoverStatusService;

    @InjectMocks
    private CategoryDataMirroringService categoryDataMirroringService;

    @Test
    public void shouldLoadCategories() throws CommunicationFailureException {

        // given
        given(categoryDataAdapter.retrieve(any())).willReturn(CATEGORY_LIST);

        // then
        categoryDataMirroringService.load();

        // then
        verify(categoryDataAdapter).retrieve(any());
        verify(categoryDataAdapter).store(any(), eq(CATEGORY_LIST));
    }

    @Test
    public void shouldFailSilentlyOnError() throws CommunicationFailureException {


        // given
        doThrow(CommunicationFailureException.class).when(categoryDataAdapter).retrieve(any());

        // when
        categoryDataMirroringService.load();

        // then
        // silent fail
        verify(failoverStatusService).markMirroringFailure(MirrorType.CATEGORY);
    }
}
