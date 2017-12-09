package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.web.registration.SparkRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Route;
import spark.Spark;

/**
 * Abstract {@link SparkRegistration} implementation for controller registration.
 * Provides common tools for every controller.
 *
 * @author Peter Smith
 */
abstract class AbstractGetControllerRegistration implements SparkRegistration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGetControllerRegistration.class);

    @Override
    public void register() {
        Spark.get(path(), route());
        LOGGER.info("[GET {}] route registered.", path());
    }

    abstract String path();

    abstract Route route();

    /**
     * Extracts given URL path parameter as integer value from Spark's {@link Request} object.
     *
     * @param request request object to extract path parameter value from
     * @param parameter parameter key as {@link PathParameter}
     * @return extracted value as integer
     */
    Integer extractIntegerParameter(Request request, PathParameter parameter) {
        return Integer.parseInt(extractParameter(request, parameter));
    }

    /**
     * Extracts given URL path parameter as long value from Spark's {@link Request} object.
     *
     * @param request request object to extract path parameter value from
     * @param parameter parameter key as {@link PathParameter}
     * @return extracted value as long
     */
    Long extractLongParameter(Request request, PathParameter parameter) {
        return Long.parseLong(extractParameter(request, parameter));
    }

    /**
     * Extracts given URL path parameter as string value from Spark's {@link Request} object.
     *
     * @param request request object to extract path parameter value from
     * @param parameter parameter key as {@link PathParameter}
     * @return extracted value as string
     */
    String extractParameter(Request request, PathParameter parameter) {
        return request.params(parameter.getParameterName());
    }
}
