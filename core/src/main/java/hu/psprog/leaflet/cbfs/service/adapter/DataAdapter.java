package hu.psprog.leaflet.cbfs.service.adapter;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;

/**
 * @param <K> key
 * @param <R> return type
 * @author Peter Smith
 */
public interface DataAdapter<K, R> {

    R retrieve(K byKey) throws CommunicationFailureException;

    void store(K key, R data);
}
