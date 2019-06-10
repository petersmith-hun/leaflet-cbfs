package hu.psprog.leaflet.cbfs.web.config.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Application info holder component.
 *
 * @author Peter Smith
 */
@Component
@PropertySource(AppInfo.PROPERTY_SOURCE)
public class AppInfo {

    private static final String APP_VERSION = "${app.version}";
    private static final String APP_BUILT = "${app.built}";
    private static final String APP_NAME = "${app.name}";
    private static final String APP_ABBREVIATION = "${app.abbreviation}";

    static final String PROPERTY_SOURCE = "classpath:version.properties";

    @JsonProperty("build.version")
    private final String appVersion;

    @JsonProperty("build.time")
    private final String builtOn;

    @JsonProperty("app.name")
    private final String appName;

    @JsonProperty("app.abbreviation")
    private final String appAbbreviation;

    @Autowired
    public AppInfo(@Value(APP_VERSION) String appVersion, @Value(APP_BUILT) String builtOn,
                   @Value(APP_NAME) String appName, @Value(APP_ABBREVIATION) String appAbbreviation) {
        this.appVersion = appVersion;
        this.builtOn = builtOn;
        this.appName = appName;
        this.appAbbreviation = appAbbreviation;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getBuiltOn() {
        return builtOn;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppAbbreviation() {
        return appAbbreviation;
    }
}
