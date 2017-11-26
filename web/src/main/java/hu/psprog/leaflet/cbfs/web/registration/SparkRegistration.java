package hu.psprog.leaflet.cbfs.web.registration;

/**
 * Spark component registration interface, that is handled by Spring DI.
 *
 * @author Peter Smith
 */
@FunctionalInterface
public interface SparkRegistration {

    /**
     * Registers any Spark component.
     */
    void register();
}
