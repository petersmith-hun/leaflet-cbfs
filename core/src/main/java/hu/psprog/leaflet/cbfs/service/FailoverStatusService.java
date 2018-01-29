package hu.psprog.leaflet.cbfs.service;

import hu.psprog.leaflet.cbfs.domain.MirrorType;
import hu.psprog.leaflet.cbfs.domain.StatusResponse;

/**
 * Failover status tracking service.
 * Tracks mirroring process and traffic takeovers.
 *
 * @author Peter Smith
 */
public interface FailoverStatusService {

    /**
     * Returns {@code true} if mirroring is currently in progress.
     *
     * @return {@code true} if mirroring is currently in progress, {@code false} otherwise
     */
    boolean isMirroring();

    /**
     * Returns {@code true} if CBFS is currently serving live traffic.
     *
     * @return {@code true} if CBFS is currently serving live traffic, {@code false} otherwise
     */
    boolean isServing();

    /**
     * Marks status as "serving" - if it's not already set.
     * Status should be restored to "standby" after some time.
     */
    void trafficReceived();

    /**
     * Marks mirroring start time.
     */
    void markMirroringStart();

    /**
     * Marks mirroring failure (if Leaflet is unavailable).
     *
     * @param mirrorType type of the failed mirror
     */
    void markMirroringFailure(MirrorType mirrorType);

    /**
     * Marks mirroring finish time.
     */
    void markMirroringFinish();

    /**
     * Returns current failover status.
     *
     * @return current failover status
     */
    StatusResponse getFailoverStatus();
}
