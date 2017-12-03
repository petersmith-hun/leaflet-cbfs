package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

/**
 * Enum for available path parameters.
 *
 * @author Peter Smith
 */
enum PathParameter {

    LINK(":link"),
    PAGE(":page"),
    CATEGORY_ID(":categoryID");

    private String parameterName;

    PathParameter(String parameterName) {
        this.parameterName = parameterName;
    }

    String getParameterName() {
        return parameterName;
    }
}
