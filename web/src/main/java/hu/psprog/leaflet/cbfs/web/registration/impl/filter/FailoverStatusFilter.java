package hu.psprog.leaflet.cbfs.web.registration.impl.filter;

import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.cbfs.web.registration.SparkRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Filter;
import spark.Spark;

/**
 * Filter to set "serving" status upon receiving live traffic.
 *
 * @author Peter Smith
 */
@Component
public class FailoverStatusFilter implements SparkRegistration {

    private static final String STATUS_ENDPOINT = "/status";

    private FailoverStatusService failoverStatusService;

    @Autowired
    public FailoverStatusFilter(FailoverStatusService failoverStatusService) {
        this.failoverStatusService = failoverStatusService;
    }

    @Override
    public void register() {
        Spark.after(statusFilter());
    }

    private Filter statusFilter() {
        return (request, response) -> {
            if (!request.pathInfo().endsWith(STATUS_ENDPOINT)) {
                failoverStatusService.trafficReceived();
            }
        };
    }
}
