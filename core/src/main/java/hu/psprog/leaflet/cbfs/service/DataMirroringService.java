package hu.psprog.leaflet.cbfs.service;

import org.springframework.core.Ordered;

/**
 * @author Peter Smith
 */
public interface DataMirroringService extends Ordered {

    void load();
}
