package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.service.snapshot.impl.DocumentSnapshotRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Route;

/**
 * Controller registration documents:
 *  GET /documents/link/:link
 *
 * @author Peter Smith
 */
@Component
public class DocumentByLinkControllerRegistration extends AbstractGetControllerRegistration {

    private static final String PATH_DOCUMENTS_BY_LINK = "/documents/link/:link";

    private DocumentSnapshotRetrievalService retrievalService;

    @Autowired
    public DocumentByLinkControllerRegistration(DocumentSnapshotRetrievalService retrievalService) {
        this.retrievalService = retrievalService;
    }

    @Override
    String path() {
        return PATH_DOCUMENTS_BY_LINK;
    }

    @Override
    Route route() {
        return (request, response) -> retrievalService.retrieve(extractParameter(request, PathParameter.LINK, String.class));
    }
}
