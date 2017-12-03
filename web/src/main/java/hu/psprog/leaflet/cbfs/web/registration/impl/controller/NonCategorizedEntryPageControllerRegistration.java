package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.service.snapshot.impl.NonCategorizedEntryPageSnapshotRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Route;

/**
 * Controller registration for non-categorized entry pages:
 *  GET /entries/page/:page
 *
 * @author Peter Smith
 */
@Component
public class NonCategorizedEntryPageControllerRegistration extends AbstractGetControllerRegistration {

    private static final String PATH_NON_CATEGORIZED_ENTRY_PAGE = "/entries/page/:page";

    private NonCategorizedEntryPageSnapshotRetrievalService retrievalService;

    @Autowired
    public NonCategorizedEntryPageControllerRegistration(NonCategorizedEntryPageSnapshotRetrievalService retrievalService) {
        this.retrievalService = retrievalService;
    }

    @Override
    String path() {
        return PATH_NON_CATEGORIZED_ENTRY_PAGE;
    }

    @Override
    Route route() {
        return (request, response) -> retrievalService.retrieve(createEntryPageKey(request));
    }

    private EntryPageKey createEntryPageKey(Request request) {
        return EntryPageKey.build(extractIntegerParameter(request, PathParameter.PAGE));
    }
}
