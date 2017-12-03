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


    <T> T extractParameter(Request request, PathParameter parameter, Class<T> asType) {
        return asType.cast(request.params(parameter.getParameterName()));
    }
}
