package hu.psprog.leaflet.cbfs.service.request.adapter;

import hu.psprog.leaflet.bridge.client.request.RequestAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * {@link RequestAdapter} implementation for CBFS.
 *
 * @author Peter Smith
 */
@Component
public class FailoverRequestAdapter implements RequestAdapter {

    private final String deviceID;

    public FailoverRequestAdapter(@Value("${cbfs.client-id}") String deviceID) {
        this.deviceID = deviceID;
    }

    @Override
    public String provideDeviceID() {
        return deviceID;
    }

    @Override
    public String provideClientID() {
        return deviceID;
    }

    @Override
    public void consumeAuthenticationToken(String token) {
        // no operation
    }
}
