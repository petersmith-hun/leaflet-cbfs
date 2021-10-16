package hu.psprog.leaflet.cbfs.service.adapter.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import hu.psprog.leaflet.cbfs.domain.Category;
import hu.psprog.leaflet.cbfs.persistence.CategoryDAO;
import hu.psprog.leaflet.cbfs.service.transformer.impl.CategoryStorageTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CategoryDataAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CategoryDataAdapterTest {

    private static final Long CATEGORY_ID = 3L;
    private static final CategoryDataModel CATEGORY_DATA_MODEL = CategoryDataModel.getBuilder()
            .withID(CATEGORY_ID)
            .build();
    private static final CategoryListDataModel RESULT = CategoryListDataModel.getBuilder()
            .withItem(CATEGORY_DATA_MODEL)
            .build();
    private static final Category CATEGORY = Category.getBuilder()
            .build();

    @Mock
    private CategoryBridgeService categoryBridgeService;

    @Mock
    private CategoryStorageTransformer categoryStorageTransformer;

    @Mock
    private CategoryDAO categoryDAO;

    @InjectMocks
    private CategoryDataAdapter categoryDataAdapter;

    @Test
    public void shouldRetrieve() throws CommunicationFailureException {

        // when
        categoryDataAdapter.retrieve(null);

        // then
        verify(categoryBridgeService).getPublicCategories();
    }

    @Test
    public void shouldStore() {

        // given
        given(categoryStorageTransformer.transform(CATEGORY_ID, CATEGORY_DATA_MODEL)).willReturn(CATEGORY);

        // when
        categoryDataAdapter.store(null, RESULT);

        // then
        verify(categoryStorageTransformer).transform(CATEGORY_ID, CATEGORY_DATA_MODEL);
        verify(categoryDAO).storeCategory(CATEGORY);
    }
}
