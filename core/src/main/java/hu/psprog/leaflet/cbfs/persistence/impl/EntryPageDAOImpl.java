package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.domain.Entry;
import hu.psprog.leaflet.cbfs.domain.EntryPage;
import hu.psprog.leaflet.cbfs.persistence.EntryDAO;
import hu.psprog.leaflet.cbfs.persistence.EntryPageDAO;
import hu.psprog.leaflet.cbfs.persistence.mapper.LinkListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link EntryPageDAO}.
 *
 * @author Peter Smith
 */
@Repository
public class EntryPageDAOImpl implements EntryPageDAO {

    private static final String PAGE_NUMBER = "page_number";
    private static final String CATEGORY_ID = "category_id";
    private static final String LINK_LIST = "link_list";

    private QueryRegistry queryRegistry;
    private NamedParameterJdbcTemplate failoverJdbcTemplate;
    private LinkListMapper linkListMapper;
    private EntryDAO entryDAO;

    @Autowired
    public EntryPageDAOImpl(QueryRegistry queryRegistry, NamedParameterJdbcTemplate failoverJdbcTemplate, LinkListMapper linkListMapper, EntryDAO entryDAO) {
        this.queryRegistry = queryRegistry;
        this.failoverJdbcTemplate = failoverJdbcTemplate;
        this.linkListMapper = linkListMapper;
        this.entryDAO = entryDAO;
    }

    @Override
    public List<Entry> getPage(int page) {
        return queryPage(queryRegistry.getPageQuery(), paramMap(page));
    }

    @Override
    public List<Entry> getPageOfCategory(int page, long categoryID) {
        return queryPage(queryRegistry.getPageOfCategoryQuery(), paramMap(page, categoryID));
    }

    @Override
    public void storePage(EntryPage entryPage) {
        failoverJdbcTemplate.execute(queryRegistry.getStorePageQuery(), paramMap(entryPage), QueryRegistry.PREPARED_STATEMENT_CALLBACK);
    }

    @Override
    public void truncate() {
        failoverJdbcTemplate.execute(queryRegistry.getTruncatePagesQuery(), QueryRegistry.PREPARED_STATEMENT_CALLBACK);
    }

    private List<Entry> queryPage(String query, Map<String, Object> paramMap) {
        return failoverJdbcTemplate.query(query, paramMap, linkListMapper).stream()
                .findFirst()
                .orElse(Collections.emptyList()).stream()
                .map(entryDAO::getByLink)
                .collect(Collectors.toList());
    }

    private Map<String, Object> paramMap(int page) {
        return paramMap(page, null);
    }

    private Map<String, Object> paramMap(int page, Long categoryID) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(PAGE_NUMBER, page);
        paramMap.put(CATEGORY_ID, categoryID);

        return paramMap;
    }

    private Map<String, Object> paramMap(EntryPage entryPage) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(PAGE_NUMBER, entryPage.getPage());
        paramMap.put(CATEGORY_ID, entryPage.getCategoryID());
        paramMap.put(LINK_LIST, entryPage.getEntries().stream()
                .map(Entry::getLink)
                .collect(Collectors.joining(",")));

        return paramMap;
    }
}
