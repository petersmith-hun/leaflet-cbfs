package hu.psprog.leaflet.cbfs.job;

import hu.psprog.leaflet.cbfs.persistence.TruncateCapableDAO;
import hu.psprog.leaflet.cbfs.service.DataMirroringService;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DataMirroringJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataMirroringJob.class);

    private List<DataMirroringService> dataMirroringServiceList;
    private List<TruncateCapableDAO> truncateCapableDAOList;
    private FailoverStatusService failoverStatusService;

    @Autowired
    public DataMirroringJob(List<DataMirroringService> dataMirroringServiceList, List<TruncateCapableDAO> truncateCapableDAOList,
                            FailoverStatusService failoverStatusService) {
        this.dataMirroringServiceList = dataMirroringServiceList;
        this.truncateCapableDAOList = truncateCapableDAOList;
        this.failoverStatusService = failoverStatusService;
    }

    @Scheduled(cron = "${mirroring.schedule}")
    public void startMirroring() {
        failoverStatusService.markMirroringStart();
        cleanUpStep();
        mirroringStep();
        failoverStatusService.markMirroringFinish();
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
