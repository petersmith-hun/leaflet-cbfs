package hu.psprog.leaflet.cbfs.persistence;

/**
 * DAO interface to add capability of truncating table.
 *
 * @author Peter Smith
 */
public interface TruncateCapableDAO {

    /**
     * Truncate table;
     */
    void truncate();
}
