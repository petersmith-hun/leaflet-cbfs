package hu.psprog.leaflet.cbfs.service;

import org.springframework.core.Ordered;

/**
 * Controls data mirroring.
 * Every data group requires an own implementation.
 * Implementations needed to be ordered, as in some cases, one depends on an other.
 *
 * @author Peter Smith
 */
public interface DataMirroringService extends Ordered {

    /**
     * Starts mirroring.
     */
    void load();
}
