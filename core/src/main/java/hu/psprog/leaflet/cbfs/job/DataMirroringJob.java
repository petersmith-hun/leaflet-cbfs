package hu.psprog.leaflet.cbfs.job;

import hu.psprog.leaflet.cbfs.job.availability.BackendAvailabilityChecker;
import hu.psprog.leaflet.cbfs.persistence.TruncateCapableDAO;
import hu.psprog.leaflet.cbfs.service.DataMirroringService;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.failover.api.domain.MirrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * Scheduled (once a day by default) job for mirroring live data.
 *
 * @author Peter Smith
 */
@Component
public class DataMirroringJob implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataMirroringJob.class);

    private List<DataMirroringService> dataMirroringServiceList;
    private List<TruncateCapableDAO> truncateCapableDAOList;
    private FailoverStatusService failoverStatusService;
    private BackendAvailabilityChecker backendAvailabilityChecker;

    @Autowired
    public DataMirroringJob(List<DataMirroringService> dataMirroringServiceList, List<TruncateCapableDAO> truncateCapableDAOList,
                            FailoverStatusService failoverStatusService, BackendAvailabilityChecker backendAvailabilityChecker) {
        this.dataMirroringServiceList = dataMirroringServiceList;
        this.truncateCapableDAOList = truncateCapableDAOList;
        this.failoverStatusService = failoverStatusService;
        this.backendAvailabilityChecker = backendAvailabilityChecker;
    }

    @Scheduled(cron = "${mirroring.schedule}")
    public void startMirroring() {
        failoverStatusService.markMirroringStart();
        if (backendAvailabilityChecker.isAvailable()) {
            cleanUpStep();
            mirroringStep();
        } else {
            failoverStatusService.markMirroringFailure(MirrorType.ALL);
            LOGGER.error("Backend unavailable - mirroring stopped, keeping current mirror.");
        }
        failoverStatusService.markMirroringFinish();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("On-startup initial mirroring started...");
        startMirroring();
    }

    private void cleanUpStep() {

        LOGGER.info("Cleaning up outdated mirror...");
        truncateCapableDAOList.forEach(TruncateCapableDAO::truncate);
        LOGGER.info("Mirror cleaned up.");
    }

    private void mirroringStep() {

        LOGGER.info("Start data mirroring...");
        dataMirroringServiceList.stream()
                .sorted(Comparator.comparing(DataMirroringService::getOrder))
                .forEach(DataMirroringService::load);
        LOGGER.info("Data mirroring finished.");
    }
}
