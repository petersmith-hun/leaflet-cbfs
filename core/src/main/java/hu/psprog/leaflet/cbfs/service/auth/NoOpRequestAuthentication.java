package hu.psprog.leaflet.cbfs.service.auth;

import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link RequestAuthentication} implementation required by Bridge to be able to authenticate requests.
 * Currently no protected endpoint is required for CBFS, so this is no-operation implementation as we won't need authentication.
 *
 * @author Peter Smith
 */
@Component
public class NoOpRequestAuthentication implements RequestAuthentication {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoOpRequestAuthentication.class);

    @Override
    public Map<String, String> getAuthenticationHeader() {
        LOGGER.info("No need for Bridge request authentication - creating no-operation implementation.");
        return new HashMap<>();
    }
}
