package hu.psprog.leaflet.cbfs.persistence.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Failover database query registry.
 *
 * @author Peter Smith
 */
@Component
class QueryRegistry {

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
}
