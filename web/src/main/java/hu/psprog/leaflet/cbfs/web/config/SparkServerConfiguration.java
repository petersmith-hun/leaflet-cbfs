package hu.psprog.leaflet.cbfs.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import spark.Spark;

/**
 * Spark's embedded Jetty server base configuration parameters.
 *
 * @author Peter Smith
 */
@Component
@PropertySource(SparkServerConfiguration.PROPERTY_SOURCE)
public class SparkServerConfiguration {

    static final String PROPERTY_SOURCE = "classpath:application.properties";

    @Value("${spark.port}")
    private int port;

    @Value("${spark.host}")
    private String host;

    public void configureSpark() {
        Spark.ipAddress(host);
        Spark.port(port);
    }
}
