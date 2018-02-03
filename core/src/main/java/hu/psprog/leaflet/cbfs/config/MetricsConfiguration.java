package hu.psprog.leaflet.cbfs.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Graphite metrics configuration.
 *
 * @author Peter Smith
 */
@Configuration
@EnableMetrics
public class MetricsConfiguration extends MetricsConfigurerAdapter {

    @Value("${metrics.enabled}")
    private boolean enabled;

    @Value("${metrics.prefix}")
    private String prefix;

    @Value("${metrics.host}")
    private String host;

    @Value("${metrics.port}")
    private int port;

    @Value("${metrics.period}")
    private long period;

    @Value("${metrics.unit}")
    private TimeUnit unit;

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        if (enabled) {
            registerReporter(buildGraphiteReporter(metricRegistry))
                    .start(period, unit);
        }
    }

    private GraphiteReporter buildGraphiteReporter(MetricRegistry metricRegistry) {
        return GraphiteReporter
                .forRegistry(metricRegistry)
                .prefixedWith(prefix)
                .build(new Graphite(host, port));
    }
}
