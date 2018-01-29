package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.cbfs.domain.FailoverStatus;
import hu.psprog.leaflet.cbfs.domain.MirrorStatus;
import hu.psprog.leaflet.cbfs.domain.MirrorType;
import hu.psprog.leaflet.cbfs.domain.StatusEntry;
import hu.psprog.leaflet.cbfs.domain.StatusResponse;
import hu.psprog.leaflet.cbfs.persistence.StatusTrackingDAO;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link FailoverStatusServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class FailoverStatusServiceImplTest {

    @Mock
    private StatusTrackingDAO statusTrackingDAO;

    private FailoverStatusService failoverStatusService;

    @Before
    public void setup() {
        failoverStatusService = new FailoverStatusServiceImpl(3, TimeUnit.SECONDS, statusTrackingDAO);
    }

    @Test
    public void shouldIsMirroringReturnTrue() {

        // given
        setFailoverStatus(FailoverStatus.MIRRORING);

        // when
        boolean result = failoverStatusService.isMirroring();

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsMirroringReturnFalse() {

        // given
        setFailoverStatus(FailoverStatus.STANDBY);

        // when
        boolean result = failoverStatusService.isMirroring();

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldIsServingReturnTrue() {

        // given
        setFailoverStatus(FailoverStatus.SERVING);

        // when
        boolean result = failoverStatusService.isServing();

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldIsServingReturnFalse() {

        // given
        setFailoverStatus(FailoverStatus.STANDBY);

        // when
        boolean result = failoverStatusService.isServing();

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldTrafficReceivedSetScheduler() throws InterruptedException {

        // when
        failoverStatusService.trafficReceived();

        // then
        assertThat(failoverStatusService.isServing(), is(true));
        Thread.sleep(3100);
        assertThat(failoverStatusService.isServing(), is(false));
        verify(statusTrackingDAO).insertStatus(FailoverStatus.SERVING);
    }

    @Test
    public void shouldTrafficReceivedSetSchedulerFromServingStateWithNullScheduler() throws InterruptedException {

        // given
        setFailoverStatus(FailoverStatus.SERVING);

        // when
        failoverStatusService.trafficReceived();

        // then
        assertThat(failoverStatusService.isServing(), is(true));
        Thread.sleep(3100);
        assertThat(failoverStatusService.isServing(), is(false));
        verify(statusTrackingDAO, never()).insertStatus(FailoverStatus.SERVING);
    }

    @Test
    public void shouldTrafficReceivedResetScheduler() throws InterruptedException {

        // when
        failoverStatusService.trafficReceived();
        Thread.sleep(1000);
        failoverStatusService.trafficReceived();

        // then
        assertThat(failoverStatusService.isServing(), is(true));
        Thread.sleep(2100);
        assertThat(failoverStatusService.isServing(), is(true));
        Thread.sleep(1100);
        assertThat(failoverStatusService.isServing(), is(false));
        verify(statusTrackingDAO).insertStatus(FailoverStatus.SERVING);
    }

    @Test
    public void shouldTrafficReceivedDoNothingIfMirroring() {

        // given
        setFailoverStatus(FailoverStatus.MIRRORING);

        // when
        failoverStatusService.trafficReceived();

        // then
        assertThat(failoverStatusService.isMirroring(), is(true));
    }

    @Test
    public void shouldMarkMirroringStart() {

        // when
        failoverStatusService.markMirroringStart();

        // then
        assertThat(failoverStatusService.isMirroring(), is(true));
        verify(statusTrackingDAO).insertStatus(FailoverStatus.MIRRORING);
    }

    @Test
    public void shouldMarkMirroringFailure() {

        // when
        failoverStatusService.markMirroringFailure(MirrorType.CATEGORY);

        // then
        verify(statusTrackingDAO).insertStatus(FailoverStatus.MIRRORING_FAILURE, MirrorType.CATEGORY.name());
    }

    @Test
    public void shouldMarkMirroringFinish() {

        // when
        failoverStatusService.markMirroringFinish();

        // then
        assertThat(failoverStatusService.isMirroring() || failoverStatusService.isServing(), is(false));
        verify(statusTrackingDAO).insertStatus(FailoverStatus.STANDBY);
    }

    @Test
    public void shouldGetFailoverStatus() {

        // given
        List<StatusEntry> statusEntryList = prepareStatusEntryList();
        List<MirrorStatus> mirrorStatusList = Collections.singletonList(MirrorStatus.getBuilder()
                .withMirrorType(MirrorType.CATEGORY)
                .withNumberOfRecords(2)
                .build());

        given(statusTrackingDAO.getStatusEntries()).willReturn(statusEntryList);
        given(statusTrackingDAO.getMirrorStatus()).willReturn(mirrorStatusList);

        // when
        StatusResponse result = failoverStatusService.getFailoverStatus();

        // then
        assertThat(result.getStatusEntryList(), equalTo(statusEntryList));
        assertThat(result.getLastCall(), equalTo(prepareDate(0)));
        assertThat(result.getLastMirroring(), equalTo(prepareDate(-5)));
        assertThat(result.getStatus(), equalTo(FailoverStatus.STANDBY));
        assertThat(result.getMirrorStatus(), equalTo(mirrorStatusList));
    }

    private List<StatusEntry> prepareStatusEntryList() {
        return Arrays.asList(
                prepareStatusEntry(false, -3),
                prepareStatusEntry(false, -1),
                prepareStatusEntry(false, 0),
                prepareStatusEntry(true, -5),
                prepareStatusEntry(false, -4),
                prepareStatusEntry(true, -7));
    }

    private StatusEntry prepareStatusEntry(boolean mirroring, int hourOffset) {
        return StatusEntry.getBuilder()
                .withStatus(mirroring
                        ? FailoverStatus.MIRRORING
                        : FailoverStatus.SERVING)
                .withCreated(prepareDate(hourOffset))
                .build();
    }

    private Date prepareDate(int hourOffset) {

        Calendar calendar = new Calendar.Builder()
                .setDate(2018, 0, 29)
                .setTimeOfDay(20, 0, 0)
                .build();
        calendar.add(Calendar.HOUR, hourOffset);

        return calendar.getTime();
    }

    private void setFailoverStatus(FailoverStatus status) {
        Field failoverStatusField = ReflectionUtils.findField(FailoverStatusServiceImpl.class, "failoverStatus");
        failoverStatusField.setAccessible(true);
        try {
            failoverStatusField.set(failoverStatusService, status);
        } catch (IllegalAccessException e) {
            fail("Failed to set failover status");
        }
    }
}