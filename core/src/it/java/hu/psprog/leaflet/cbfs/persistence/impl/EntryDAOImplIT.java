package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.domain.Entry;
import hu.psprog.leaflet.cbfs.persistence.EntryDAO;
import hu.psprog.leaflet.cbfs.persistence.config.FailoverPersistenceITConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link EntryDAOImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FailoverPersistenceITConfig.class)
@ActiveProfiles(FailoverPersistenceITConfig.PERSISTENCE_IT)
public class EntryDAOImplIT {

    private static final String ENTRY_2_LINK = "testentry-2-171130";
    private static final String NEW_ENTRY_LINK = "new-entry-171130";
    private static final String NEW_ENTRY_CONTENT = "{...entry json ...}";

    @Autowired
    private EntryDAO entryDAO;

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void shouldGetEntryByLink() {

        // when
        Entry result = entryDAO.getByLink(ENTRY_2_LINK);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getLink(), equalTo(ENTRY_2_LINK));
        assertThat(result.getContent().contains(ENTRY_2_LINK), is(true));
    }

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void shouldStoreEntry() {

        // given
        Entry entryToStore = prepareControlEntry();

        // when
        entryDAO.storeEntry(entryToStore);

        // then
        Entry storedEntry = entryDAO.getByLink(NEW_ENTRY_LINK);
        assertThat(storedEntry, equalTo(entryToStore));
    }

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES)
    public void shouldTruncateEntries() {

        // when
        entryDAO.truncate();

        // then
        assertThat(entryDAO.getByLink(ENTRY_2_LINK), nullValue());
    }

    private Entry prepareControlEntry() {
        return Entry.getBuilder()
                .withLink(NEW_ENTRY_LINK)
                .withContent(NEW_ENTRY_CONTENT)
                .build();
    }
}
