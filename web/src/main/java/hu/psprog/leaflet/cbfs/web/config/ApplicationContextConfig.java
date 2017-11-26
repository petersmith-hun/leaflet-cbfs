package hu.psprog.leaflet.cbfs.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Application context base configuration.
 *
 * @author Peter Smith
 */
@Configuration
@ComponentScan(basePackages = {ApplicationContextConfig.WEB_PACKAGE})
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
