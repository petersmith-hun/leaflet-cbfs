package hu.psprog.leaflet.cbfs.persistence;

import hu.psprog.leaflet.cbfs.domain.Entry;

/**
 * {@link Entry} related DAO operations.
 *
 * @author Peter Smith
 */
public interface EntryDAO {

    /**
     * Retrieves {@link Entry} identified by given link.
     *
     * @param link link to identify {@link Entry}
     * @return Entry identified by link
     */
    Entry getByLink(String link);

    /**
     * Stores given {@link Entry} object.
     *
     * @param entry {@link Entry} object to store
     */
    void storeEntry(Entry entry);

    /**
     * Truncate table of {@link Entry} objects.
     */
    void truncate();
}
