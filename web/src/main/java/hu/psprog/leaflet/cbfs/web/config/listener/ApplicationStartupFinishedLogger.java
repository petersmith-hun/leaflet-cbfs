package hu.psprog.leaflet.cbfs.web.config.listener;

import hu.psprog.leaflet.cbfs.web.config.domain.AppInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Application startup finished logger.
 * Logs application version and build date. Should be called right after application context initialization has finished.
 *
 * @author Peter Smith
 */
@Component
public class ApplicationStartupFinishedLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartupFinishedLogger.class);

    private AppInfo appInfo;

    @Autowired
    public ApplicationStartupFinishedLogger(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public void logStartupFinished() {
        LOGGER.info("Application loaded successfully, running version v{}, built on {}", appInfo.getAppVersion(), appInfo.getBuiltOn());
    }
}
