package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import hu.psprog.leaflet.cbfs.persistence.CategoryDAO;
import hu.psprog.leaflet.cbfs.service.snapshot.SnapshotRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link SnapshotRetrievalService} for category list.
 *
 * @author Peter Smith
 */
@Service
public class CategoryListSnapshotRetrievalService implements SnapshotRetrievalService<Void> {

    private CategoryDAO categoryDAO;

    @Autowired
    public CategoryListSnapshotRetrievalService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public String retrieve(Void key) {
        return null;
    }
}
