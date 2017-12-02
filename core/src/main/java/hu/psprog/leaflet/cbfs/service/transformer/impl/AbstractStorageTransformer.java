package hu.psprog.leaflet.cbfs.service.transformer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.cbfs.service.transformer.StorageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract {@link StorageTransformer} implementation that is able to convert source object to JSON.
 *
 * @author Peter Smith
 */
abstract class AbstractStorageTransformer<K, S, T> implements StorageTransformer<K, S, T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStorageTransformer.class);

    @Autowired
    private ObjectMapper objectMapper;

    String convertToJSON(S source) {

        String jsonContent = null;
        try {
            jsonContent = objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to convert source to JSON.", e);
        }

        return jsonContent;
    }
}
