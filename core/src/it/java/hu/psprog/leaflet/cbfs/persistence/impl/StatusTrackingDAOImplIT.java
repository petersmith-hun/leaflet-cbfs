package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.persistence.StatusTrackingDAO;
import hu.psprog.leaflet.cbfs.persistence.config.FailoverPersistenceITConfig;
import hu.psprog.leaflet.failover.api.domain.FailoverStatus;
import hu.psprog.leaflet.failover.api.domain.MirrorStatus;
import hu.psprog.leaflet.failover.api.domain.MirrorType;
import hu.psprog.leaflet.failover.api.domain.StatusEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link StatusTrackingDAO}.
 *
 * @author Peter Smith
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FailoverPersistenceITConfig.class)
@ActiveProfiles(FailoverPersistenceITConfig.PERSISTENCE_IT)
public class StatusTrackingDAOImplIT {

    @Autowired
    private StatusTrackingDAO statusTrackingDAO;

    @Test
    @Transactional
    public void shouldInsertStatusWithoutParameter() {

        // when
        statusTrackingDAO.insertStatus(FailoverStatus.STANDBY);

        // then
        List<StatusEntry> current = statusTrackingDAO.getStatusEntries();
        assertThat(current.size(), equalTo(1));
        assertThat(current.get(0).getStatus(), equalTo(FailoverStatus.STANDBY));
        assertThat(current.get(0).getParameter(), nullValue());
    }

    @Test
    @Transactional
    public void shouldInsertStatusWithParameter() {

        // given
        String parameter = "DOCUMENT";

        // when
        statusTrackingDAO.insertStatus(FailoverStatus.MIRRORING_FAILURE, parameter);

        // then
        List<StatusEntry> current = statusTrackingDAO.getStatusEntries();
        assertThat(current.size(), equalTo(1));
        assertThat(current.get(0).getStatus(), equalTo(FailoverStatus.MIRRORING_FAILURE));
        assertThat(current.get(0).getParameter(), equalTo(parameter));
    }

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_STATUS)
    public void shouldGetStatusEntries() {

        // when
        List<StatusEntry> result = statusTrackingDAO.getStatusEntries();

        // then
        assertThat(result.size(), equalTo(4));
        assertThat(result.stream()
                .map(StatusEntry::getStatus)
                .collect(Collectors.toList())
                .containsAll(Arrays.asList(FailoverStatus.values())), is(true));
        assertThat(result.stream()
                .filter(statusEntry -> statusEntry.getStatus() == FailoverStatus.MIRRORING_FAILURE)
                .map(StatusEntry::getParameter)
                .findFirst()
                .orElse(null), equalTo("DOCUMENT"));
    }

    @Test
    @Transactional
    @Sql({
            FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_STATUS,
            FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_CATEGORIES,
            FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS,
            FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_ENTRIES,
            FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_PAGES
    })
    public void shouldGetMirrorStatus() {

        // when
        List<MirrorStatus> result = statusTrackingDAO.getMirrorStatus();

        // then
        assertThat(result.containsAll(prepareMirrorStatusList()), is(true));
    }

    private List<MirrorStatus> prepareMirrorStatusList() {
        return Arrays.asList(
                prepareMirrorStatus(MirrorType.CATEGORY, 5),
                prepareMirrorStatus(MirrorType.DOCUMENT, 3),
                prepareMirrorStatus(MirrorType.ENTRY, 3),
                prepareMirrorStatus(MirrorType.ENTRY_PAGE, 4));
    }

    private MirrorStatus prepareMirrorStatus(MirrorType type, int numberOfRecords) {
        return MirrorStatus.getBuilder()
                .withMirrorType(type)
                .withNumberOfRecords(numberOfRecords)
                .build();
    }
}
