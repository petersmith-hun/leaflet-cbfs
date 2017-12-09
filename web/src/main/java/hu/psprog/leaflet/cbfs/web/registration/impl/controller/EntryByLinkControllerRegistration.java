package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.service.snapshot.impl.EntrySnapshotRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Route;

/**
 * Controller registration for entries:
 *  GET /entries/link/:link
 *
 * @author Peter Smith
 */
@Component
public class EntryByLinkControllerRegistration extends AbstractGetControllerRegistration {

    private static final String PATH_ENTRIES_BY_LINK = "/entries/link/:link";

    private EntrySnapshotRetrievalService retrievalService;

    @Autowired
    public EntryByLinkControllerRegistration(EntrySnapshotRetrievalService retrievalService) {
        this.retrievalService = retrievalService;
    }

    @Override
    String path() {
        return PATH_ENTRIES_BY_LINK;
    }

    @Override
    Route route() {
        return (request, response) -> retrievalService.retrieve(extractParameter(request, PathParameter.LINK));
    }
}
