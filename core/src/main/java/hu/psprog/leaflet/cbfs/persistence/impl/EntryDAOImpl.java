package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.domain.Entry;
import hu.psprog.leaflet.cbfs.persistence.EntryDAO;
import hu.psprog.leaflet.cbfs.persistence.mapper.EntryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link EntryDAO}.
 *
 * @author Peter Smith
 */
@Repository
public class EntryDAOImpl implements EntryDAO {

    private static final String LINK = "link";
    private static final String CONTENT = "content";

    private QueryRegistry queryRegistry;
    private EntryMapper entryMapper;
    private NamedParameterJdbcTemplate failoverJdbcTemplate;

    @Autowired
    public EntryDAOImpl(QueryRegistry queryRegistry, EntryMapper entryMapper, NamedParameterJdbcTemplate failoverJdbcTemplate) {
        this.queryRegistry = queryRegistry;
        this.entryMapper = entryMapper;
        this.failoverJdbcTemplate = failoverJdbcTemplate;
    }

    @Override
    public Entry getByLink(String link) {
        return failoverJdbcTemplate.queryForObject(queryRegistry.getEntryQuery(), paramMap(link), entryMapper);
    }

    @Override
    public void storeEntry(Entry entry) {
        failoverJdbcTemplate.execute(queryRegistry.getStoreEntryQuery(), paramMap(entry), QueryRegistry.PREPARED_STATEMENT_CALLBACK);
    }

    @Override
    public void truncate() {
        failoverJdbcTemplate.execute(queryRegistry.getTruncateEntriesQuery(), QueryRegistry.PREPARED_STATEMENT_CALLBACK);
    }

    private Map<String, Object> paramMap(String link) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(LINK, link);

        return paramMap;
    }

    private Map<String, Object> paramMap(Entry entry) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(LINK, entry.getLink());
        paramMap.put(CONTENT, entry.getContent());

        return paramMap;
    }
}
