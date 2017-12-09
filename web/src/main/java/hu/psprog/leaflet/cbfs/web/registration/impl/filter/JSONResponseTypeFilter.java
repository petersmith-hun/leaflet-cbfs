package hu.psprog.leaflet.cbfs.web.registration.impl.filter;

import hu.psprog.leaflet.cbfs.web.registration.SparkRegistration;
import org.springframework.stereotype.Component;
import spark.Filter;
import spark.Spark;

/**
 * After-response filter to set content type to 'application/json'.
 *
 * @author Peter Smith
 */
@Component
public class JSONResponseTypeFilter implements SparkRegistration {

    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    @Override
    public void register() {
        Spark.after(jsonContentTypeFilter());
    }

    private Filter jsonContentTypeFilter() {
        return (request, response) -> response.type(CONTENT_TYPE_APPLICATION_JSON);
    }
}
