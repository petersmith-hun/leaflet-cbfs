package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.web.registration.SparkRegistration;
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

    @Override
    public void register() {
        Spark.get(path(), route());
    }

    abstract String path();

    abstract Route route();

    Integer extractIntegerParameter(Request request, PathParameter parameter) {
        return Integer.parseInt(extractParameter(request, parameter));
    }

    Long extractLongParameter(Request request, PathParameter parameter) {
        return Long.parseLong(extractParameter(request, parameter));
    }

    String extractParameter(Request request, PathParameter parameter) {
        return request.params(parameter.getParameterName());
    }
}
