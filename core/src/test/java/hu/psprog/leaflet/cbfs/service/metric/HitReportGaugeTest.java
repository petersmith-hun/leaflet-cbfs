package hu.psprog.leaflet.cbfs.service.metric;

import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link HitReportGauge}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class HitReportGaugeTest {

    @Mock
    private FailoverStatusService failoverStatusService;

    @InjectMocks
    private HitReportGauge hitReportGauge;

    @Test
    public void shouldReturn1WhenServing() {

        // given
        given(failoverStatusService.isServing()).willReturn(true);

        // then
        int result = hitReportGauge.getValue();

        // then
        assertThat(result, equalTo(1));
    }

    @Test
    public void shouldReturn0WhenNotServing() {

        // given
        given(failoverStatusService.isServing()).willReturn(false);

        // then
        int result = hitReportGauge.getValue();

        // then
        assertThat(result, equalTo(0));
    }
}