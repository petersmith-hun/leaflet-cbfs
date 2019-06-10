package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.cbfs.web.config.domain.AppInfo;
import hu.psprog.leaflet.cbfs.web.registration.impl.conversion.AppInfoToNestedMapConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Route;

import java.util.Map;

/**
 * Controller registration for Spring Boot Actuator-like info endpoint.
 *  GET /info
 *
 * @author Peter Smith
 */
@Component
public class AppInfoControllerRegistration extends AbstractGetControllerRegistration {

    private static final String PATH_INFO = "/info";

    private AppInfo appInfo;
    private ObjectMapper objectMapper;
    private AppInfoToNestedMapConverter appInfoToNestedMapConverter;

    @Autowired
    public AppInfoControllerRegistration(AppInfo appInfo, ObjectMapper objectMapper, AppInfoToNestedMapConverter appInfoToNestedMapConverter) {
        this.appInfo = appInfo;
        this.objectMapper = objectMapper;
        this.appInfoToNestedMapConverter = appInfoToNestedMapConverter;
    }

    @Override
    String path() {
        return PATH_INFO;
    }

    @Override
    Route route() {
        return (request, response) -> {
            Map<String, Map<String, String>> appInfoNestedMap = appInfoToNestedMapConverter.convert(appInfo);
            return objectMapper.writeValueAsString(appInfoNestedMap);
        };
    }
}
