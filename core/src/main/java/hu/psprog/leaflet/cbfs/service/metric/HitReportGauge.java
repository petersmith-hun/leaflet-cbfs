package hu.psprog.leaflet.cbfs.service.metric;

import com.codahale.metrics.annotation.Gauge;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Gauge metric to report CBFS hits.
 *
 * @author Peter Smith
 */
@Component
public class HitReportGauge {

    private static final int HIT_VALUE = 1;
    private static final int RESET_VALUE = 0;

    private FailoverStatusService failoverStatusService;

    @Autowired
    public HitReportGauge(FailoverStatusService failoverStatusService) {
        this.failoverStatusService = failoverStatusService;
    }

    @Gauge(name = "snapshot.retrieval.hit.report", absolute = true)
    public Integer getValue() {
        return failoverStatusService.isServing()
                ? HIT_VALUE
                : RESET_VALUE;
    }
}
