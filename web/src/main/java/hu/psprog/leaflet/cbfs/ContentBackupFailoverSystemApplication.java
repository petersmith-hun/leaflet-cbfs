package hu.psprog.leaflet.cbfs;

import hu.psprog.leaflet.cbfs.web.config.ApplicationContextConfig;
import hu.psprog.leaflet.cbfs.web.config.SparkServerConfiguration;
import hu.psprog.leaflet.cbfs.web.config.listener.ApplicationStartupFinishedLogger;
import hu.psprog.leaflet.cbfs.web.registration.SparkRegistrationAgent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Content Backup Failover System (CBFS) application entry point.
 *
 * @author Peter Smith
 */
public class ContentBackupFailoverSystemApplication {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextConfig.class);
        context.getBean(SparkServerConfiguration.class).configureSpark();
        context.getBean(SparkRegistrationAgent.class).start();
        context.getBean(ApplicationStartupFinishedLogger.class).logStartupFinished();
    }
}
