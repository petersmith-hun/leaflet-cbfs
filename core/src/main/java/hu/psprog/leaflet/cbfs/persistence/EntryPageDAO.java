package hu.psprog.leaflet.cbfs.persistence;

import hu.psprog.leaflet.cbfs.domain.Entry;
import hu.psprog.leaflet.cbfs.domain.EntryPage;

import java.util.List;
import java.util.Set;

/**
 * {@link EntryPage} related DAO operations.
 *
 * @author Peter Smith
 */
public interface EntryPageDAO extends TruncateCapableDAO {

    /**
     * Retrieves {@link List} of {@link Entry} objects for given page number.
     *
     * @param page page number
     * @return List of Entry objects
     */
    List<Entry> getPage(int page);

    /**
     * Retrieves {@link List} of {@link Entry} objects for given page number and category ID.
     *
     * @param page page number
     * @param categoryID category ID
     * @return List of Entry objects
     */
    List<Entry> getPageOfCategory(int page, long categoryID);

    /**
     * Retrieves all entry links that are stored under the pages.
     *
     * @return list of links
     */
    Set<String> collectAllEntryLinks();

    /**
     * Stores given {@link EntryPage} object.
     *
     * @param entryPage the {@link EntryPage} object to store
     */
    void storePage(EntryPage entryPage);
}
