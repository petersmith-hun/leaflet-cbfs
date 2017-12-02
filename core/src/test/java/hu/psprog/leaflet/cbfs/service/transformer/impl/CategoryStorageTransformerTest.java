package hu.psprog.leaflet.cbfs.service.transformer.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.cbfs.domain.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CategoryStorageTransformer}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryStorageTransformerTest {

    private static final Long KEY = 1L;
    private static final String TITLE = "title";

    @InjectMocks
    private CategoryStorageTransformer categoryStorageTransformer;

    @Test
    public void shouldTransform() {

        // given
        CategoryDataModel categoryDataModel = CategoryDataModel.getBuilder()
                .withTitle(TITLE)
                .build();

        // when
        Category result = categoryStorageTransformer.transform(KEY, categoryDataModel);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(KEY));
        assertThat(result.getTitle(), equalTo(TITLE));
    }
}
