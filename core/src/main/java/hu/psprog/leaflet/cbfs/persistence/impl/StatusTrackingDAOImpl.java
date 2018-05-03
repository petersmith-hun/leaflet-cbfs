package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.persistence.StatusTrackingDAO;
import hu.psprog.leaflet.cbfs.persistence.mapper.MirrorStatusMapper;
import hu.psprog.leaflet.cbfs.persistence.mapper.StatusEntryMapper;
import hu.psprog.leaflet.failover.api.domain.FailoverStatus;
import hu.psprog.leaflet.failover.api.domain.MirrorStatus;
import hu.psprog.leaflet.failover.api.domain.StatusEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link StatusTrackingDAO}.
 *
 * @author Peter Smith
 */
@Repository
public class StatusTrackingDAOImpl implements StatusTrackingDAO {

    private NamedParameterJdbcTemplate failoverJdbcTemplate;
    private QueryRegistry queryRegistry;
    private StatusEntryMapper statusEntryMapper;
    private MirrorStatusMapper mirrorStatusMapper;

    @Autowired
    public StatusTrackingDAOImpl(NamedParameterJdbcTemplate failoverJdbcTemplate, QueryRegistry queryRegistry,
                                 StatusEntryMapper statusEntryMapper, MirrorStatusMapper mirrorStatusMapper) {
        this.failoverJdbcTemplate = failoverJdbcTemplate;
        this.queryRegistry = queryRegistry;
        this.statusEntryMapper = statusEntryMapper;
        this.mirrorStatusMapper = mirrorStatusMapper;
    }

    @Override
    public void insertStatus(FailoverStatus status) {
        insertStatus(status, null);
    }

    @Override
    public void insertStatus(FailoverStatus status, String parameter) {
        failoverJdbcTemplate.execute(queryRegistry.getInsertStatus(), paramMap(status, parameter), QueryRegistry.PREPARED_STATEMENT_CALLBACK);
    }

    @Override
    public List<StatusEntry> getStatusEntries() {
        return failoverJdbcTemplate.query(queryRegistry.getStatusEntries(), statusEntryMapper);
    }

    @Override
    public List<MirrorStatus> getMirrorStatus() {
        return failoverJdbcTemplate.queryForObject(queryRegistry.getMirrorStatus(), new HashMap<>(), mirrorStatusMapper);
    }

    private Map<String, Object> paramMap(FailoverStatus status, String parameter) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", status.name());
        paramMap.put("parameter", parameter);

        return paramMap;
    }
}
