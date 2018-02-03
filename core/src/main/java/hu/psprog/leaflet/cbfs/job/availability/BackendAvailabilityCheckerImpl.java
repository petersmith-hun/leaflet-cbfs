package hu.psprog.leaflet.cbfs.job.availability;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.cbfs.service.adapter.impl.CategoryDataAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link BackendAvailabilityChecker}.
 * Currently calls public categories endpoint - later it should be changed to an explicit status endpoint.
 *
 * @author Peter Smith
 */
@Component
public class BackendAvailabilityCheckerImpl extends BackendAvailabilityChecker {

    private CategoryDataAdapter categoryDataAdapter;

    @Autowired
    public BackendAvailabilityCheckerImpl(CategoryDataAdapter categoryDataAdapter) {
        this.categoryDataAdapter = categoryDataAdapter;
    }

    @Override
    protected void call() throws CommunicationFailureException {
        categoryDataAdapter.retrieve(null);
    }
}
