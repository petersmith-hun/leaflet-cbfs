package hu.psprog.leaflet.cbfs.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.FixedValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.UUID;

/**
 * @author Peter Smith
 */
@Configuration
@ComponentScan
@ComponentScan(basePackages = {
        "hu.psprog.leaflet.bridge.client",
        "hu.psprog.leaflet.bridge.adapter",
        "hu.psprog.leaflet.bridge.service"
})
public class FailoverBridgeConfiguration {

    @Bean
    public Client jacksonClient() {

        return ClientBuilder.newBuilder()
                .register(JacksonJsonProvider.class)
                .register(MultiPartFeature.class)
                .build();
    }

    @Bean
    @DependsOn("jacksonClient")
    public WebTarget webTarget(Client bridgeClient, @Value("${bridge.base-url}") String baseUrl) {

        return bridgeClient.target(baseUrl);
    }

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
