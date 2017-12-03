package hu.psprog.leaflet.cbfs.service.snapshot;

/**
 * Interface for services that can return a stored snapshot by their key.
 *
 * @param <K> key type
 * @author Peter Smith
 */
@FunctionalInterface
public interface SnapshotRetrievalService<K> {

    /**
     * Returns stored snapshot as {@link String}.
     *
     * @param key key of the snapshot item to retrieve
     * @return stored snapshot as String
     */
    String retrieve(K key);
}
