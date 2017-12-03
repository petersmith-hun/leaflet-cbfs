package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.domain.Entry;
import hu.psprog.leaflet.cbfs.domain.EntryPage;
import hu.psprog.leaflet.cbfs.persistence.EntryPageDAO;
import hu.psprog.leaflet.cbfs.persistence.config.FailoverPersistenceITConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
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

    private static final String ENTRY_1_LINK = "testentry-1-171130";
    private static final String ENTRY_2_LINK = "testentry-2-171130";
    private static final String ENTRY_3_LINK = "testentry-3-171130";

    private static final int PAGE_NUMBER = 2;
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
        List<Entry> result = entryPageDAO.getPage(page);

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(2));
        assertThat(result.stream().anyMatch(entry -> entry.getLink().equals(ENTRY_1_LINK)), is(true));
        assertThat(result.stream().anyMatch(entry -> entry.getLink().equals(ENTRY_2_LINK)), is(true));
    }

    @Test
    @Transactional
    @Sql({FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_PAGES, FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES})
    public void shouldReturnNonCategorizedSecondPage() {

        // given
        int page = 2;

        // when
        List<Entry> result = entryPageDAO.getPage(page);

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.stream().anyMatch(entry -> entry.getLink().equals(ENTRY_3_LINK)), is(true));
    }

    @Test
    @Transactional
    @Sql({FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_PAGES, FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES})
    public void shouldReturnCategorizedFirstPage() {

        // given
        int page = 1;
        long categoryID = 1;

        // when
        List<Entry> result = entryPageDAO.getPageOfCategory(page, categoryID);

        // then
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(2));
        assertThat(result.stream().anyMatch(entry -> entry.getLink().equals(ENTRY_1_LINK)), is(true));
        assertThat(result.stream().anyMatch(entry -> entry.getLink().equals(ENTRY_3_LINK)), is(true));
    }

    @Test
    @Transactional
    @Sql({FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_PAGES, FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES})
    public void shouldReturnEmptyListForNonExistingPage() {

        // given
        int page = 4;

        // when
        List<Entry> result = entryPageDAO.getPage(page);

        // then
        assertThat(result, notNullValue());
        assertThat(result.isEmpty(), is(true));
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
        List<Entry> storedPage = entryPageDAO.getPage(PAGE_NUMBER);
        assertThat(storedPage.size(), equalTo(2));
        assertThat(storedPage.stream().anyMatch(entry -> entry.getLink().equals(ENTRY_2_LINK)), is(true));
        assertThat(storedPage.stream().anyMatch(entry -> entry.getLink().equals(ENTRY_3_LINK)), is(true));
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
        List<Entry> storedPage = entryPageDAO.getPageOfCategory(PAGE_NUMBER, CATEGORY_ID);
        assertThat(storedPage.size(), equalTo(2));
        assertThat(storedPage.stream().anyMatch(entry -> entry.getLink().equals(ENTRY_2_LINK)), is(true));
        assertThat(storedPage.stream().anyMatch(entry -> entry.getLink().equals(ENTRY_3_LINK)), is(true));
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
                .withPage(PAGE_NUMBER)
                .withCategoryID(categorized
                        ? CATEGORY_ID
                        : null)
                .withEntries(Arrays.asList(prepareEntry(ENTRY_2_LINK), prepareEntry(ENTRY_3_LINK)))
                .build();
    }

    private Entry prepareEntry(String link) {
        return Entry.getBuilder()
                .withLink(link)
                .withContent(link + "-content")
                .build();
    }
}
