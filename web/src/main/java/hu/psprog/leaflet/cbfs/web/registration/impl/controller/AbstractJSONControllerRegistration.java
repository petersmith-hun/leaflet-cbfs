package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.cbfs.web.registration.SparkRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract {@link SparkRegistration} implementation for controller registration.
 * Provides common tools for every controller.
 *
 * @author Peter Smith
 */
abstract class AbstractJSONControllerRegistration<T> implements SparkRegistration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJSONControllerRegistration.class);

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Converts controller's response of type T to JSON string.
     *
     * @param controllerResponse raw response of controller
     * @return response converter to JSON string
     */
    String json(T controllerResponse) {

        String convertedResponse = null;
        try {
            convertedResponse = objectMapper.writeValueAsString(controllerResponse);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Failed to convert response to JSON. Returning empty response.");
        }

        return convertedResponse;
    }
}
