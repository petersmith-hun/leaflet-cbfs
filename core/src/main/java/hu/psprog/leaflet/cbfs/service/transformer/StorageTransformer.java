package hu.psprog.leaflet.cbfs.service.transformer;

/**
 *
 * @param <K> key type
 * @param <S> source type
 * @param <T> target type
 * @author Peter Smith
 */
public interface StorageTransformer<K, S, T> {

    T transform(K key, S source);
}
