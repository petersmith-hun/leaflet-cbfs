package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.service.snapshot.impl.CategorizedEntryPageSnapshotRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Route;

/**
 * Controller registration for categorized entry pages:
 *  GET /entries/:categoryID/page/:page
 *
 * @author Peter Smith
 */
@Component
public class CategorizedEntryPageControllerRegistration extends AbstractGetControllerRegistration {

    private static final String PATH_CATEGORIZED_ENTRY_PAGE = "/entries/category/:categoryID/page/:page";

    private CategorizedEntryPageSnapshotRetrievalService retrievalService;

    @Autowired
    public CategorizedEntryPageControllerRegistration(CategorizedEntryPageSnapshotRetrievalService retrievalService) {
        this.retrievalService = retrievalService;
    }

    @Override
    String path() {
        return PATH_CATEGORIZED_ENTRY_PAGE;
    }

    @Override
    Route route() {
        return (request, response) -> retrievalService.retrieve(createEntryPageKey(request));
    }

    private EntryPageKey createEntryPageKey(Request request) {
        return EntryPageKey.build(extractIntegerParameter(request, PathParameter.PAGE), extractLongParameter(request, PathParameter.CATEGORY_ID));
    }
}
