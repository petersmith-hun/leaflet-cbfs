package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.domain.EntryPage;
import hu.psprog.leaflet.cbfs.persistence.EntryPageDAO;
import hu.psprog.leaflet.cbfs.persistence.mapper.LinkListMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link EntryPageDAO}.
 *
 * @author Peter Smith
 */
@Repository
public class EntryPageDAOImpl implements EntryPageDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryPageDAOImpl.class);

    private static final String PAGE_NUMBER = "page_number";
    private static final String CATEGORY_ID = "category_id";
    private static final String LINK_LIST = "link_list";
    private static final String CONTENT = "content";

    private QueryRegistry queryRegistry;
    private NamedParameterJdbcTemplate failoverJdbcTemplate;
    private LinkListMapper linkListMapper;

    @Autowired
    public EntryPageDAOImpl(QueryRegistry queryRegistry, NamedParameterJdbcTemplate failoverJdbcTemplate, LinkListMapper linkListMapper) {
        this.queryRegistry = queryRegistry;
        this.failoverJdbcTemplate = failoverJdbcTemplate;
        this.linkListMapper = linkListMapper;
    }

    @Override
    public String getPage(int page) {
        return queryPage(queryRegistry.getPageQuery(), paramMap(page));
    }

    @Override
    public String getPageOfCategory(int page, long categoryID) {
        return queryPage(queryRegistry.getPageOfCategoryQuery(), paramMap(page, categoryID));
    }

    @Override
    public Set<String> collectAllEntryLinks() {
        return failoverJdbcTemplate.query(queryRegistry.getAllEntryLinks(), linkListMapper).stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public void storePage(EntryPage entryPage) {
        failoverJdbcTemplate.execute(queryRegistry.getStorePageQuery(), paramMap(entryPage), QueryRegistry.PREPARED_STATEMENT_CALLBACK);
    }

    @Override
    public void truncate() {
        failoverJdbcTemplate.execute(queryRegistry.getTruncatePagesQuery(), QueryRegistry.PREPARED_STATEMENT_CALLBACK);
    }

    private String queryPage(String query, Map<String, Object> paramMap) {

        String content = StringUtils.EMPTY;
        try {
            content = failoverJdbcTemplate.queryForObject(query, paramMap, String.class);
        } catch (Exception e) {
            LOGGER.warn("Failed to extract page, returning empty response.", e);
        }

        return content;
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
                .collect(Collectors.joining(",")));
        paramMap.put(CONTENT, entryPage.getContent());

        return paramMap;
    }
}
