package hu.psprog.leaflet.cbfs.persistence;

import hu.psprog.leaflet.cbfs.domain.EntryPage;

import java.util.Set;

/**
 * {@link EntryPage} related DAO operations.
 *
 * @author Peter Smith
 */
public interface EntryPageDAO extends TruncateCapableDAO {

    /**
     * Retrieves snapshot content as String for given page number.
     *
     * @param page page number
     * @return snapshot content
     */
    String getPage(int page);

    /**
     * Retrieves snapshot content as String for given page number and category ID.
     *
     * @param page page number
     * @param categoryID category ID
     * @return snapshot content
     */
    String getPageOfCategory(int page, long categoryID);

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
