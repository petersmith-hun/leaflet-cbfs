package hu.psprog.leaflet.cbfs.web.registration.impl.controller;

import hu.psprog.leaflet.cbfs.service.snapshot.impl.CategoryListSnapshotRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Route;

/**
 * Controller registration for public categories:
 *  GET /categories/public
 *
 * @author Peter Smith
 */
@Component
public class PublicCategoriesControllerRegistration extends AbstractGetControllerRegistration {

    private static final String PATH_CATEGORIES_PUBLIC = "/categories/public";

    private CategoryListSnapshotRetrievalService retrievalService;

    @Autowired
    public PublicCategoriesControllerRegistration(CategoryListSnapshotRetrievalService retrievalService) {
        this.retrievalService = retrievalService;
    }

    @Override
    String path() {
        return PATH_CATEGORIES_PUBLIC;
    }

    @Override
    Route route() {
        return (request, response) -> retrievalService.retrieve(null);
    }
}
