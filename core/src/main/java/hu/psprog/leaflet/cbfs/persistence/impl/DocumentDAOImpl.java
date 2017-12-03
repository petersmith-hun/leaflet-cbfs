package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.domain.Document;
import hu.psprog.leaflet.cbfs.persistence.DocumentDAO;
import hu.psprog.leaflet.cbfs.persistence.mapper.DocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link DocumentDAO};
 *
 * @author Peter Smith
 */
@Repository
public class DocumentDAOImpl implements DocumentDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentDAOImpl.class);

    private static final String LINK = "link";
    private static final String CONTENT = "content";

    private QueryRegistry queryRegistry;
    private NamedParameterJdbcTemplate failoverJdbcTemplate;
    private DocumentMapper documentMapper;

    @Autowired
    public DocumentDAOImpl(QueryRegistry queryRegistry, NamedParameterJdbcTemplate failoverJdbcTemplate, DocumentMapper documentMapper) {
        this.queryRegistry = queryRegistry;
        this.failoverJdbcTemplate = failoverJdbcTemplate;
        this.documentMapper = documentMapper;
    }

    @Override
    public Document getByLink(String link) {

        Document document = null;
        try {
            document = failoverJdbcTemplate.queryForObject(queryRegistry.getDocumentQuery(), paramMap(link), documentMapper);
        } catch (EmptyResultDataAccessException exc) {
            LOGGER.warn("Document [{}] not found", link, exc);
        }

        return Optional.ofNullable(document)
                .orElse(Document.getBuilder().build());
    }

    @Override
    public void storeDocument(Document document) {
        failoverJdbcTemplate.execute(queryRegistry.getStoreDocumentQuery(), paramMap(document), QueryRegistry.PREPARED_STATEMENT_CALLBACK);
    }

    @Override
    public void truncate() {
        failoverJdbcTemplate.execute(queryRegistry.getTruncateDocumentsQuery(), QueryRegistry.PREPARED_STATEMENT_CALLBACK);
    }

    private Map<String, Object> paramMap(String link) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(LINK, link);

        return paramMap;
    }

    private Map<String, Object> paramMap(Document document) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(LINK, document.getLink());
        paramMap.put(CONTENT, document.getContent());

        return paramMap;
    }
}
