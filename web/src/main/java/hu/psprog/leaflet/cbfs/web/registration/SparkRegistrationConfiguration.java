package hu.psprog.leaflet.cbfs.web.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * Registers all discovered {@link SparkRegistration} instances.
 *
 * @author Peter Smith
 */
@Component
public class SparkRegistrationConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkRegistrationConfiguration.class);
    private static final Consumer<SparkRegistration> REGISTRATION_LOGGING =
            sparkRegistration -> LOGGER.info("Registering {} ...", sparkRegistration.getClass().getName());

    private List<SparkRegistration> registrations;

    @Autowired
    public SparkRegistrationConfiguration(List<SparkRegistration> registrations) {
        this.registrations = registrations;
    }

    /**
     * Starts registration process.
     */
    public void start() {
        registrations.stream()
                .peek(REGISTRATION_LOGGING)
                .forEach(SparkRegistration::register);

        LOGGER.info("Registration finished.");
    }
}
