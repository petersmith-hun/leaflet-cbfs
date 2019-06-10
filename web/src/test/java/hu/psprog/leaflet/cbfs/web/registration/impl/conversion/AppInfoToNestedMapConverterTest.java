package hu.psprog.leaflet.cbfs.web.registration.impl.conversion;

import hu.psprog.leaflet.cbfs.web.config.domain.AppInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link AppInfoToNestedMapConverter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class AppInfoToNestedMapConverterTest {

    private static final String APP_VERSION = "v1.0";
    private static final String BUILT_ON = "2019-06-10";
    private static final String APP_NAME = "content backup";
    private static final String APP_ABBREVIATION = "cbfs";
    private static final AppInfo APP_INFO = new AppInfo(APP_VERSION, BUILT_ON, APP_NAME, APP_ABBREVIATION);

    private static final Map<String, Map<String, String>> EXPECTED_APP_INFO_MAP = Map.of(
            "app", Map.of(
                    "name", APP_NAME,
                    "abbreviation", APP_ABBREVIATION),
            "build", Map.of(
                    "version", APP_VERSION,
                    "time", BUILT_ON));

    @InjectMocks
    private AppInfoToNestedMapConverter converter;

    @Test
    public void shouldConvertAppInfoToNesterMap() {

        // when
        Map<String, Map<String, String>> result = converter.convert(APP_INFO);

        // then
        assertThat(result, equalTo(EXPECTED_APP_INFO_MAP));
    }
}
