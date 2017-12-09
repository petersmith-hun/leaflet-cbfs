package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.web.registration.SparkRegistration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import spark.Request;
import spark.Route;
import spark.Spark;

import java.util.Optional;

/**
 * Abstract {@link SparkRegistration} implementation for controller registration.
 * Provides common tools for every controller.
 *
 * @author Peter Smith
 */
abstract class AbstractGetControllerRegistration implements SparkRegistration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGetControllerRegistration.class);

    @Value("${spark.context-root}")
    private String contextRoot;

    @Override
    public void register() {
        Spark.get(pathWithContextRoot(), route());
        LOGGER.info("[GET {}] route registered.", pathWithContextRoot());
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

    private String pathWithContextRoot() {
        return getContextRoot() + path();
    }

    private String getContextRoot() {
        return Optional.ofNullable(contextRoot)
                .orElse(StringUtils.EMPTY);
    }
}
