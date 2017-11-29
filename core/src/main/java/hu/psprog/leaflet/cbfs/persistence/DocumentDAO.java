package hu.psprog.leaflet.cbfs.persistence;

import hu.psprog.leaflet.cbfs.domain.Document;

/**
 * {@link Document} related DAO operations.
 * 
 * @author Peter Smith
 */
public interface DocumentDAO {

    /**
     * Retrieves {@link Document} identified by given link.
     *
     * @param link link to identify {@link Document}
     * @return Document identified by link
     */
    Document getByLink(String link);

    /**
     * Stores given {@link Document} object.
     *
     * @param document {@link Document} object to store
     */
    void storeDocument(Document document);

    /**
     * Truncate table of {@link Document} objects.
     */
    void truncate();
}
