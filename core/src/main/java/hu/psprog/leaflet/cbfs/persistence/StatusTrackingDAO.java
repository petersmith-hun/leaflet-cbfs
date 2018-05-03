package hu.psprog.leaflet.cbfs.persistence;

import hu.psprog.leaflet.failover.api.domain.FailoverStatus;
import hu.psprog.leaflet.failover.api.domain.MirrorStatus;
import hu.psprog.leaflet.failover.api.domain.StatusEntry;

import java.util.List;

/**
 * Status tracking related DAO operations.
 *
 * @author Peter Smith
 */
public interface StatusTrackingDAO {

    /**
     * Insert a new status change without extra informational parameter into the status tracking table.
     *
     * @param status current status
     */
    void insertStatus(FailoverStatus status);

    /**
     * Insert a new status change with extra informational parameter into the status tracking table.
     *
     * @param status current status
     * @param parameter informational parameter
     */
    void insertStatus(FailoverStatus status, String parameter);

    /**
     * Retrieves all existing status entries from status tracking table.
     *
     * @return List of {@link StatusEntry} objects
     */
    List<StatusEntry> getStatusEntries();

    /**
     * Returns mirror status, containing number of records by mirror types.
     *
     * @return mirror status
     */
    List<MirrorStatus> getMirrorStatus();
}
