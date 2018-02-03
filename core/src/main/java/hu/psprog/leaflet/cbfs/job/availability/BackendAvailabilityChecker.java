package hu.psprog.leaflet.cbfs.job.availability;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks if the backend application is available for mirroring.
 *
 * @author Peter Smith
 */
public abstract class BackendAvailabilityChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendAvailabilityChecker.class);

    /**
     * Calls backend application to check availability.
     * Any thrown exception means unavailability.
     *
     * @return {@code true} if the backend application is available, {@code false} otherwise
     */
    public boolean isAvailable() {

        boolean available = true;
        try {
            call();
        } catch (Exception exc) {
            LOGGER.error("Backend application unavailable - check call failed with exception", exc);
            available = false;
        }

        return available;
    }

    /**
     * Calls check endpoint.
     *
     * @throws CommunicationFailureException if backend cannot be reached by HTTP request
     */
    protected abstract void call() throws CommunicationFailureException;
}
