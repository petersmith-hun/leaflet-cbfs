package hu.psprog.leaflet.cbfs.job;

import hu.psprog.leaflet.cbfs.persistence.TruncateCapableDAO;
import hu.psprog.leaflet.cbfs.service.DataMirroringService;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link DataMirroringJob}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DataMirroringJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TruncateMock.class);

    private static final String TRUNCATE_1 = "truncate-1";
    private static final String TRUNCATE_2 = "truncate-2";
    private static final String MIRROR_1 = "mirror-1";
    private static final String MIRROR_2 = "mirror-2";
    private static final String MIRROR_3 = "mirror-3";

    private DataMirroringJob dataMirroringJob;
    private List<String> callLogger = new ArrayList<>();

    @Mock
    private FailoverStatusService failoverStatusService;

    @Before
    public void setup() {
        dataMirroringJob = new DataMirroringJob(
                Arrays.asList(new MirrorMock(2, MIRROR_2), new MirrorMock(1, MIRROR_1), new MirrorMock(3, MIRROR_3)),
                Arrays.asList(new TruncateMock(TRUNCATE_1), new TruncateMock(TRUNCATE_2)),
                failoverStatusService);
    }

    @Test
    public void shouldStartMirroring() {

        // when
        dataMirroringJob.startMirroring();

        // then
        assertThat(callLogger.size(), equalTo(5));
        assertThat(callLogger.get(0), equalTo(TRUNCATE_1));
        assertThat(callLogger.get(1), equalTo(TRUNCATE_2));
        assertThat(callLogger.get(2), equalTo(MIRROR_1));
        assertThat(callLogger.get(3), equalTo(MIRROR_2));
        assertThat(callLogger.get(4), equalTo(MIRROR_3));
    }

    private class MirrorMock implements DataMirroringService {

        private int order;
        private String mockID;

        private MirrorMock(int order, String mockID) {
            this.order = order;
            this.mockID = mockID;
        }

        @Override
        public void load() {
            callLogger.add(mockID);
            LOGGER.info("Load for [{}] called", mockID);
        }

        @Override
        public int getOrder() {
            return order;
        }
    }

    private class TruncateMock implements TruncateCapableDAO {

        private String mockID;

        private TruncateMock(String mockID) {
            this.mockID = mockID;
        }

        @Override
        public void truncate() {
            callLogger.add(mockID);
            LOGGER.info("Truncate for [{}] called", mockID);
        }
    }
}
