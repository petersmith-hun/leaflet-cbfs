package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.cbfs.domain.Category;
import hu.psprog.leaflet.cbfs.persistence.CategoryDAO;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link CategoryListSnapshotRetrievalService}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryListSnapshotRetrievalServiceTest {

    private static final String EXPECTED_JSON_VALUE = "{\"body\":{\"categories\":[{\"id\":1,\"title\":\"category-1\"},{\"id\":2,\"title\":\"category-2\"}]}}";

    @Mock
    private CategoryDAO categoryDAO;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private CategoryListSnapshotRetrievalService service;

    @Test
    public void shouldRetrieve() {

        // given
        given(categoryDAO.getCategories()).willReturn(prepareCategoryList());

        // when
        String result = service.retrieve(null);

        // then
        assertThat(result, equalTo(EXPECTED_JSON_VALUE));
    }

    @Test
    public void shouldRetrieveEmptyResponseOnJSONProcessingFailure() throws JsonProcessingException {

        // given
        given(categoryDAO.getCategories()).willReturn(prepareCategoryList());
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(any());

        // when
        String result = service.retrieve(null);

        // then
        assertThat(result, equalTo(StringUtils.EMPTY));
    }

    private List<Category> prepareCategoryList() {
        return Arrays.asList(prepareCategory(1L), prepareCategory(2L));
    }

    private Category prepareCategory(Long categoryID) {
        return Category.getBuilder()
                .withId(categoryID)
                .withTitle("category-" + categoryID)
                .build();
    }
}
