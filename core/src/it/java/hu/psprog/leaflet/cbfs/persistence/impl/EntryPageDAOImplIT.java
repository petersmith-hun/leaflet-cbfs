package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.domain.EntryPage;
import hu.psprog.leaflet.cbfs.persistence.EntryPageDAO;
import hu.psprog.leaflet.cbfs.persistence.config.FailoverPersistenceITConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration test for {@link EntryPageDAOImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FailoverPersistenceITConfig.class)
@ActiveProfiles(FailoverPersistenceITConfig.PERSISTENCE_IT)
public class EntryPageDAOImplIT {

    private static final String NON_CATEGORIZED_FIRST_PAGE_CONTENT = "{json.page.1.null}";
    private static final String NON_CATEGORIZED_SECOND_PAGE_CONTENT = "{json.page.2.null}";
    private static final String CATEGORIZED_FIRST_PAGE_CONTENT = "{json.page.1.1}";
    private static final String NEW_NON_CATEGORIZED_PAGED_CONTENT = "{json.page.4.null}";
    private static final String NEW_CATEGORIZED_PAGED_CONTENT = "{json.page.4.5}";

    private static final String ENTRY_1_LINK = "testentry-1-171130";
    private static final String ENTRY_2_LINK = "testentry-2-171130";
    private static final String ENTRY_3_LINK = "testentry-3-171130";

    private static final int NEW_PAGE_NUMBER = 4;
    private static final long CATEGORY_ID = 5;

    @Autowired
    private EntryPageDAO entryPageDAO;

    @Test
    @Transactional
    @Sql({FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_PAGES, FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES})
    public void shouldReturnNonCategorizedFirstPage() {

        // given
        int page = 1;

        // when
        String result = entryPageDAO.getPage(page);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(NON_CATEGORIZED_FIRST_PAGE_CONTENT));
    }

    @Test
    @Transactional
    @Sql({FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_PAGES, FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES})
    public void shouldReturnNonCategorizedSecondPage() {

        // given
        int page = 2;

        // when
        String result = entryPageDAO.getPage(page);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(NON_CATEGORIZED_SECOND_PAGE_CONTENT));
    }

    @Test
    @Transactional
    @Sql({FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_PAGES, FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES})
    public void shouldReturnCategorizedFirstPage() {

        // given
        int page = 1;
        long categoryID = 1;

        // when
        String result = entryPageDAO.getPageOfCategory(page, categoryID);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(CATEGORIZED_FIRST_PAGE_CONTENT));
    }

    @Test
    @Transactional
    @Sql({FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_PAGES, FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES})
    public void shouldReturnEmptyResponseForNonExistingPage() {

        // given
        int page = 5;

        // when
        String result = entryPageDAO.getPage(page);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(StringUtils.EMPTY));
    }

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void shouldStoreNonCategorizedPage() {

        // given
        EntryPage entryPage = prepareEntryPage(false);

        // when
        entryPageDAO.storePage(entryPage);

        // then
        String storedPage = entryPageDAO.getPage(NEW_PAGE_NUMBER);
        assertThat(storedPage, equalTo(NEW_NON_CATEGORIZED_PAGED_CONTENT));
    }

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_PAGES)
    public void shouldReturnListOfLinks() {

        // when
        Set<String> result = entryPageDAO.collectAllEntryLinks();

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(3));
        assertThat(result.containsAll(Arrays.asList(ENTRY_1_LINK, ENTRY_2_LINK, ENTRY_3_LINK)), is(true));
    }

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void shouldStoreCategorizedPage() {


        // given
        EntryPage entryPage = prepareEntryPage(true);

        // when
        entryPageDAO.storePage(entryPage);

        // then
        String storedPage = entryPageDAO.getPageOfCategory(NEW_PAGE_NUMBER, CATEGORY_ID);
        assertThat(storedPage, equalTo(NEW_CATEGORIZED_PAGED_CONTENT));
    }

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_PAGES)
    public void shouldTruncatePages() {

        // when
        entryPageDAO.truncate();

        // then
        assertThat(entryPageDAO.getPage(1).isEmpty(), is(true));
    }

    private EntryPage prepareEntryPage(boolean categorized) {
        return EntryPage.getBuilder()
                .withPage(NEW_PAGE_NUMBER)
                .withCategoryID(categorized
                        ? CATEGORY_ID
                        : null)
                .withEntries(Arrays.asList(ENTRY_2_LINK, ENTRY_3_LINK))
                .withContent(categorized
                        ? NEW_CATEGORIZED_PAGED_CONTENT
                        : NEW_NON_CATEGORIZED_PAGED_CONTENT)
                .build();
    }
}
