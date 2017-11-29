package hu.psprog.leaflet.cbfs.persistence;

import hu.psprog.leaflet.cbfs.domain.Entry;
import hu.psprog.leaflet.cbfs.domain.EntryPage;

import java.util.List;

/**
 * {@link EntryPage} related DAO operations.
 *
 * @author Peter Smith
 */
public interface EntryPageDAO {

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
     * Stores given {@link EntryPage} object.
     *
     * @param entryPage the {@link EntryPage} object to store
     */
    void storePage(EntryPage entryPage);

    /**
     * Truncates table of {@link EntryPage} objects.
     */
    void truncate();
}
