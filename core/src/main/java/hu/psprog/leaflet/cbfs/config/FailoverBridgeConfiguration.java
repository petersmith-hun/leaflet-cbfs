package hu.psprog.leaflet.cbfs.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.handler.InvocationFactory;
import hu.psprog.leaflet.bridge.client.handler.ResponseReader;
import hu.psprog.leaflet.bridge.client.impl.BridgeClientImpl;
import hu.psprog.leaflet.bridge.client.impl.InvocationFactoryImpl;
import hu.psprog.leaflet.bridge.client.impl.ResponseReaderImpl;
import hu.psprog.leaflet.bridge.client.request.RequestAdapter;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import hu.psprog.leaflet.bridge.client.request.strategy.CallStrategy;
import hu.psprog.leaflet.bridge.client.request.strategy.impl.DeleteCallStrategy;
import hu.psprog.leaflet.bridge.client.request.strategy.impl.GetCallStrategy;
import hu.psprog.leaflet.bridge.client.request.strategy.impl.PostCallStrategy;
import hu.psprog.leaflet.bridge.client.request.strategy.impl.PutCallStrategy;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import hu.psprog.leaflet.bridge.service.DocumentBridgeService;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.bridge.service.impl.CategoryBridgeServiceImpl;
import hu.psprog.leaflet.bridge.service.impl.DocumentBridgeServiceImpl;
import hu.psprog.leaflet.bridge.service.impl.EntryBridgeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.List;

/**
 * Custom configuration for Bridge.
 *
 * @author Peter Smith
 */
@Configuration
public class FailoverBridgeConfiguration {

    private final String leafletHostURL;

    public FailoverBridgeConfiguration(@Value("${bridge.clients.leaflet.host-url}") String leafletHostURL) {
        this.leafletHostURL = leafletHostURL;
    }

    @Bean
    @Autowired
    public EntryBridgeService entryBridgeService(BridgeClient bridgeClient) {
        return new EntryBridgeServiceImpl(bridgeClient);
    }

    @Bean
    @Autowired
    public CategoryBridgeService categoryBridgeService(BridgeClient bridgeClient) {
        return new CategoryBridgeServiceImpl(bridgeClient);
    }

    @Bean
    @Autowired
    public DocumentBridgeService documentBridgeService(BridgeClient bridgeClient) {
        return new DocumentBridgeServiceImpl(bridgeClient);
    }

    @Bean
    @Autowired
    BridgeClient bridgeClient(WebTarget webTarget, InvocationFactory invocationFactory, ResponseReader responseReader) {
        return new BridgeClientImpl(webTarget, invocationFactory, responseReader);
    }

    @Bean
    WebTarget webTarget() {
        return ClientBuilder.newBuilder()
                .register(JacksonJsonProvider.class)
                .build()
                .target(leafletHostURL);
    }

    @Bean
    CallStrategy getCallStrategy() {
        return new GetCallStrategy();
    }

    @Bean
    CallStrategy postCallStrategy() {
        return new PostCallStrategy();
    }

    @Bean
    CallStrategy putCallStrategy() {
        return new PutCallStrategy();
    }

    @Bean
    CallStrategy deleteCallStrategy() {
        return new DeleteCallStrategy();
    }

    @Bean
    @Autowired
    InvocationFactory invocationFactory(RequestAuthentication requestAuthentication, List<CallStrategy> callStrategyList, RequestAdapter requestAdapter) {
        return new InvocationFactoryImpl(requestAuthentication, callStrategyList, requestAdapter);
    }

    @Bean
    @Autowired
    ResponseReader responseReader(RequestAdapter requestAdapter) {
        return new ResponseReaderImpl(requestAdapter);
    }
}
