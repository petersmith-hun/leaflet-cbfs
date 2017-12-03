package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.cbfs.service.DataMirroringService;
import hu.psprog.leaflet.cbfs.service.adapter.impl.CategoryDataAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link DataMirroringService} implementation for category data mirroring.
 *
 * @author Peter Smith
 */
@Service
public class CategoryDataMirroringService implements DataMirroringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDataMirroringService.class);

    private CategoryDataAdapter categoryDataAdapter;

    @Autowired
    public CategoryDataMirroringService(CategoryDataAdapter categoryDataAdapter) {
        this.categoryDataAdapter = categoryDataAdapter;
    }

    @Override
    public void load() {
        try {
            LOGGER.info("Start collecting categories...");
            WrapperBodyDataModel<CategoryListDataModel> result = categoryDataAdapter.retrieve(null);
            categoryDataAdapter.store(null, result);
            LOGGER.info("Collecting categories done.");
        } catch (Exception e) {
            LOGGER.error("Failed to collect category data.", e);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
