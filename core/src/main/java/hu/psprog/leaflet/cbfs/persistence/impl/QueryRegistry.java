package hu.psprog.leaflet.cbfs.persistence.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

/**
 * Failover database query registry.
 *
 * @author Peter Smith
 */
@Component
class QueryRegistry {

    static final PreparedStatementCallback<Boolean> PREPARED_STATEMENT_CALLBACK = PreparedStatement::execute;

    @Value("${getPage}")
    private String pageQuery;

    @Value("${getPageOfCategory}")
    private String pageOfCategoryQuery;

    @Value("${storePage}")
    private String storePageQuery;

    @Value("${truncatePages}")
    private String truncatePagesQuery;

    @Value("${getEntry}")
    private String entryQuery;
    
    @Value("${storeEntry}")
    private String storeEntryQuery;
    
    @Value("${truncateEntries}")
    private String truncateEntriesQuery;

    @Value("${getDocument}")
    private String documentQuery;

    @Value("${storeDocument}")
    private String storeDocumentQuery;

    @Value("${truncateDocuments}")
    private String truncateDocumentsQuery;

    @Value("${getCategories}")
    private String categoriesQuery;

    @Value("${storeCategory}")
    private String storeCategoryQuery;

    @Value("${truncateCategories}")
    private String truncateCategoriesQuery;

    String getPageQuery() {
        return pageQuery;
    }

    String getPageOfCategoryQuery() {
        return pageOfCategoryQuery;
    }

    String getStorePageQuery() {
        return storePageQuery;
    }

    String getTruncatePagesQuery() {
        return truncatePagesQuery;
    }

    String getEntryQuery() {
        return entryQuery;
    }

    String getStoreEntryQuery() {
        return storeEntryQuery;
    }

    String getTruncateEntriesQuery() {
        return truncateEntriesQuery;
    }

    String getDocumentQuery() {
        return documentQuery;
    }

    String getStoreDocumentQuery() {
        return storeDocumentQuery;
    }

    String getTruncateDocumentsQuery() {
        return truncateDocumentsQuery;
    }

    String getCategoriesQuery() {
        return categoriesQuery;
    }

    String getStoreCategoryQuery() {
        return storeCategoryQuery;
    }

    String getTruncateCategoriesQuery() {
        return truncateCategoriesQuery;
    }
}
