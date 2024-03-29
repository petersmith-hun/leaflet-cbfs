package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.cbfs.service.adapter.impl.CategoryDataAdapter;
import hu.psprog.leaflet.failover.api.domain.MirrorType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CategoryDataMirroringService}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CategoryDataMirroringServiceTest {

    private static final CategoryListDataModel CATEGORY_LIST = CategoryListDataModel.getBuilder().build();

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
