package hu.psprog.leaflet.cbfs.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hu.psprog.leaflet.cbfs.config.DataSourceConfig;
import hu.psprog.leaflet.cbfs.config.FailoverBridgeConfiguration;
import hu.psprog.leaflet.cbfs.config.ServiceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

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

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setTimeZone(TimeZone.getDefault());

        return mapper;
    }

    @Bean
    public static  PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
