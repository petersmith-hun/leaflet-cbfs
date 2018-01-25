package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Route;

/**
 * Status controller registration.
 *  GET /status
 *
 * @author Peter Smith
 */
@Component
public class StatusControllerRegistration extends AbstractGetControllerRegistration {

    private static final String PATH_STATUS = "/status";

    private FailoverStatusService failoverStatusService;
    private ObjectMapper objectMapper;

    @Autowired
    public StatusControllerRegistration(FailoverStatusService failoverStatusService, ObjectMapper objectMapper) {
        this.failoverStatusService = failoverStatusService;
        this.objectMapper = objectMapper;
    }

    @Override
    String path() {
        return PATH_STATUS;
    }

    @Override
    Route route() {
        return (request, response) -> objectMapper.writeValueAsString(failoverStatusService.getFailoverStatus());
    }
}
