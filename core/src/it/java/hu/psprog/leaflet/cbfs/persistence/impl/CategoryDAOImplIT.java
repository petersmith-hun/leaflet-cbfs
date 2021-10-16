package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.domain.Category;
import hu.psprog.leaflet.cbfs.persistence.CategoryDAO;
import hu.psprog.leaflet.cbfs.persistence.config.FailoverPersistenceITConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link CategoryDAOImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FailoverPersistenceITConfig.class)
@ActiveProfiles(FailoverPersistenceITConfig.PERSISTENCE_IT)
public class CategoryDAOImplIT {

    private static final long NEW_CATEGORY_ID = 7L;
    private static final String NEW_CATEGORY_TITLE = "Category #7";

    @Autowired
    private CategoryDAO categoryDAO;

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_CATEGORIES)
    public void shouldGetCategories() {

        // when
        List<Category> result = categoryDAO.getCategories();

        // then
        assertThat(result, notNullValue());
        assertThat(result.containsAll(expectedCategoryList()), is(true));
    }

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_CATEGORIES)
    public void shouldStoreCategory() {

        // given
        Category newCategory = prepareCategory();

        // when
        categoryDAO.storeCategory(newCategory);

        // then
        List<Category> current = categoryDAO.getCategories();
        assertThat(current.size(), equalTo(6));
        assertThat(current.contains(newCategory), is(true));
    }

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_CATEGORIES)
    public void shouldTruncateCategories() {

        // when
        categoryDAO.truncate();

        // then
        List<Category> current = categoryDAO.getCategories();
        assertThat(current.isEmpty(), is(true));
    }

    private Category prepareCategory() {
        return Category.getBuilder()
                .withId(NEW_CATEGORY_ID)
                .withTitle(NEW_CATEGORY_TITLE)
                .build();
    }

    private List<Category> expectedCategoryList() {

        return IntStream.range(1, 6)
                .mapToObj(value -> Category.getBuilder()
                        .withId(value)
                        .withTitle("Category #" + value)
                        .build())
                .collect(Collectors.toList());
    }
}
