package hu.psprog.leaflet.cbfs.web.config.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Application startup finished logger.
 * Logs application version and build date. Should be called right after application context initialization has finished.
 *
 * @author Peter Smith
 */
@Component
@PropertySource(ApplicationStartupFinishedLogger.PROPERTY_SOURCE)
public class ApplicationStartupFinishedLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartupFinishedLogger.class);
    private static final String APP_VERSION = "${app.version}";
    private static final String APP_BUILT = "${app.built}";

    static final String PROPERTY_SOURCE = "classpath:version.properties";

    private final String appVersion;
    private final String builtOn;

    @Autowired
    public ApplicationStartupFinishedLogger(@Value(APP_VERSION) String appVersion, @Value(APP_BUILT) String builtOn) {
        this.appVersion = appVersion;
        this.builtOn = builtOn;
    }

    public void logStartupFinished() {
        LOGGER.info("Application loaded successfully, running version v{}, built on {}", appVersion, builtOn);
    }
}
