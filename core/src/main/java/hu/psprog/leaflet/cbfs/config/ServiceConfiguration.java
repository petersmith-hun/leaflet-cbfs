package hu.psprog.leaflet.cbfs.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * CBFS service configuration.
 *
 * @author Peter Smith
 */
@Configuration
@Import(MirroringConfiguration.class)
@ComponentScan(basePackages = {
        "hu.psprog.leaflet.cbfs.service",
        "hu.psprog.leaflet.cbfs.job"
})
public class ServiceConfiguration {
}
