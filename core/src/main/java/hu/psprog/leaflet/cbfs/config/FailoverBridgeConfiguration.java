package hu.psprog.leaflet.cbfs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.FixedValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Custom configuration for Bridge.
 *
 * @author Peter Smith
 */
@Configuration
@ComponentScan(basePackages = "hu.psprog.leaflet.bridge")
@EnableConfigurationProperties
public class FailoverBridgeConfiguration {

    @Bean
    public HttpServletRequest dummyHttpServletRequest(@Value("${cbfs.client-id}") UUID clientID) {
        return (HttpServletRequest) createFixedValueEnhancer(HttpServletRequest.class, () -> clientID).create();
    }

    @Bean
    public HttpServletResponse dummyHttpServletResponse() {
        return (HttpServletResponse) createFixedValueEnhancer(HttpServletResponse.class, () -> null).create();
    }

    private Enhancer createFixedValueEnhancer(Class<?> forClass, FixedValue callback) {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(forClass);
        enhancer.setCallback(callback);

        return enhancer;
    }
}
