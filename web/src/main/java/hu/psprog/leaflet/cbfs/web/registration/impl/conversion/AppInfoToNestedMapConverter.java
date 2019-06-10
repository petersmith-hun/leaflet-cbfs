package hu.psprog.leaflet.cbfs.web.registration.impl.conversion;

import hu.psprog.leaflet.cbfs.web.config.domain.AppInfo;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Converts {@link AppInfo} object to {@link Map}.
 *
 * @author Peter Smith
 */
@Component
public class AppInfoToNestedMapConverter {

    /**
     * Converts the given {@link AppInfo} object to nested string Map.
     *
     * @param appInfo {@link AppInfo} object to be converted
     * @return nested map of values stored in {@link AppInfo} object
     */
    public Map<String, Map<String, String>> convert(AppInfo appInfo) {

        return Map.of(
                "app", Map.of(
                        "name", appInfo.getAppName(),
                        "abbreviation", appInfo.getAppAbbreviation()),
                "build", Map.of(
                        "version", appInfo.getAppVersion(),
                        "time", appInfo.getBuiltOn()));
    }
}
