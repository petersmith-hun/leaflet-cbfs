package hu.psprog.leaflet.cbfs.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.cbfs.config.DataSourceConfig;
import hu.psprog.leaflet.cbfs.config.FailoverBridgeConfiguration;
import hu.psprog.leaflet.cbfs.config.ServiceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application context base configuration.
 *
 * @author Peter Smith
 */
@Configuration
@ComponentScan(basePackages = {ApplicationContextConfig.WEB_PACKAGE})
@Import(value = {
        DataSourceConfig.class,
        FailoverBridgeConfiguration.class,
        ServiceConfiguration.class
})
@EnableScheduling
public class ApplicationContextConfig {

    static final String WEB_PACKAGE = "hu.psprog.leaflet.cbfs.web";

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public static  PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
