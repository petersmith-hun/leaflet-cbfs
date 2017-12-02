package hu.psprog.leaflet.cbfs.service.transformer;

/**
 * Interface for converters able to convert any object of type S to type of T with key of type K.
 * T should be a type specified in hu.psprog.leaflet.cbfs.domain package.
 * S is any type returned by Bridge (specified in API).
 *
 * @param <K> key type
 * @param <S> source type
 * @param <T> target type
 * @author Peter Smith
 */
public interface StorageTransformer<K, S, T> {

    /**
     * Converts given object with given key.
     *
     * @param key key object
     * @param source source object
     * @return converted object
     */
    T transform(K key, S source);
}
