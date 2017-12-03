package hu.psprog.leaflet.cbfs.service.adapter;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;

/**
 * Adapter to request and store data from backend.
 * Implementations should be working the following way:
 *  - retrieve: calls Leaflet backend with the given key (if needed)
 *  - store: stores received data after transformation in failover database
 *
 * @param <K> key data item key - the key the backend called with should be the same as the one stored in the failover database
 * @param <R> return type raw data type received from backend
 * @author Peter Smith
 */
public interface DataAdapter<K, R> {

    /**
     * Retrieves requested data from backend.
     *
     * @param byKey key (identifier) of the requested data item
     * @return raw data returned by backend
     * @throws CommunicationFailureException on Bridge communication failure
     */
    R retrieve(K byKey) throws CommunicationFailureException;

    /**
     * Stores returned data in failover database.
     *
     * @param key key (identifier) of the data item to store
     * @param data raw data to store (conversion required)
     */
    void store(K key, R data);
}
