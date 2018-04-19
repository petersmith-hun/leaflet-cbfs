package hu.psprog.leaflet.cbfs.service.impl;

import com.codahale.metrics.annotation.Counted;
import hu.psprog.leaflet.cbfs.persistence.StatusTrackingDAO;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.failover.api.domain.FailoverStatus;
import hu.psprog.leaflet.failover.api.domain.MirrorType;
import hu.psprog.leaflet.failover.api.domain.StatusEntry;
import hu.psprog.leaflet.failover.api.domain.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of {@link FailoverStatusService}.
 *
 * @author Peter Smith
 */
@Service
public class FailoverStatusServiceImpl implements FailoverStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FailoverStatusServiceImpl.class);
    private static final ScheduledExecutorService STATUS_RESET_SCHEDULER = Executors.newSingleThreadScheduledExecutor();
    private static final Comparator<StatusEntry> STATUS_ENTRY_COMPARATOR_BY_CREATION_DATE = Comparator.comparing(StatusEntry::getCreated);

    private FailoverStatus failoverStatus = FailoverStatus.STANDBY;
    private ScheduledFuture<?> scheduler;
    private int statusResetDelay;
    private TimeUnit delayTimeUnit;
    private StatusTrackingDAO statusTrackingDAO;

    @Autowired
    public FailoverStatusServiceImpl(@Value("${failover-status.reset-delay.interval}") int statusResetDelay,
                                     @Value("${failover-status.reset-delay.unit}") TimeUnit delayTimeUnit,
                                     StatusTrackingDAO statusTrackingDAO) {
        this.statusResetDelay = statusResetDelay;
        this.delayTimeUnit = delayTimeUnit;
        this.statusTrackingDAO = statusTrackingDAO;
    }

    @Override
    public boolean isMirroring() {
        return failoverStatus == FailoverStatus.MIRRORING;
    }

    @Override
    public boolean isServing() {
        return failoverStatus == FailoverStatus.SERVING;
    }

    @Override
    @Counted(name = "snapshot.retrieval.call", absolute = true, monotonic = true)
    public void trafficReceived() {

        if (isServing()) {
            if (Objects.nonNull(scheduler)) {
                scheduler.cancel(true);
            }
            scheduler = setStatusResetScheduler();
        } else if (!isMirroring()){
            changeStatus(FailoverStatus.SERVING);
            scheduler = setStatusResetScheduler();
        }
    }

    @Override
    public void markMirroringStart() {
        changeStatus(FailoverStatus.MIRRORING);
    }

    @Override
    public void markMirroringFailure(MirrorType mirrorType) {
        statusTrackingDAO.insertStatus(FailoverStatus.MIRRORING_FAILURE, mirrorType.name());
    }

    @Override
    public void markMirroringFinish() {
        changeStatus(FailoverStatus.STANDBY);
    }

    @Override
    public StatusResponse getFailoverStatus() {

        List<StatusEntry> statusEntryList = statusTrackingDAO.getStatusEntries();

        return StatusResponse.getBuilder()
                .withLastCall(extractLastCallDate(statusEntryList))
                .withLastMirroring(extractLastMirroringDate(statusEntryList))
                .withStatusEntryList(statusEntryList)
                .withStatus(failoverStatus)
                .withMirrorStatus(statusTrackingDAO.getMirrorStatus())
                .build();
    }

    private Date extractLastCallDate(List<StatusEntry> statusEntryList) {
        return statusEntryList.stream()
                .filter(statusEntry -> FailoverStatus.SERVING == statusEntry.getStatus())
                .max(STATUS_ENTRY_COMPARATOR_BY_CREATION_DATE)
                .map(StatusEntry::getCreated)
                .orElse(null);
    }

    private Date extractLastMirroringDate(List<StatusEntry> statusEntryList) {
        return statusEntryList.stream()
                .filter(statusEntry -> FailoverStatus.MIRRORING == statusEntry.getStatus())
                .max(STATUS_ENTRY_COMPARATOR_BY_CREATION_DATE)
                .map(StatusEntry::getCreated)
                .orElse(null);
    }

    private void changeStatus(FailoverStatus status) {
        failoverStatus = status;
        statusTrackingDAO.insertStatus(status);
        LOGGER.info("Failover status changed to [{}]", failoverStatus);
    }

    private ScheduledFuture<?> setStatusResetScheduler() {
        return STATUS_RESET_SCHEDULER.schedule(() -> changeStatus(FailoverStatus.STANDBY), statusResetDelay, delayTimeUnit);
    }
}
