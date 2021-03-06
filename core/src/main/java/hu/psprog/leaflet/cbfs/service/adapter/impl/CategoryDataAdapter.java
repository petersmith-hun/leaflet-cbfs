package hu.psprog.leaflet.cbfs.service.adapter.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import hu.psprog.leaflet.cbfs.persistence.CategoryDAO;
import hu.psprog.leaflet.cbfs.service.adapter.DataAdapter;
import hu.psprog.leaflet.cbfs.service.transformer.impl.CategoryStorageTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link DataAdapter} for categories.
 *  - data source is {@link CategoryBridgeService#getPublicCategories()} method
 *  - data key is Void we request all public categories without any filtering
 *
 * @author Peter Smith
 */
@Component
public class CategoryDataAdapter implements DataAdapter<Void, CategoryListDataModel> {

    private CategoryBridgeService categoryBridgeService;
    private CategoryStorageTransformer transformer;
    private CategoryDAO categoryDAO;

    @Autowired
    public CategoryDataAdapter(CategoryBridgeService categoryBridgeService, CategoryStorageTransformer transformer, CategoryDAO categoryDAO) {
        this.categoryBridgeService = categoryBridgeService;
        this.transformer = transformer;
        this.categoryDAO = categoryDAO;
    }

    @Override
    public CategoryListDataModel retrieve(Void byKey) throws CommunicationFailureException {
        return categoryBridgeService.getPublicCategories();
    }

    @Override
    public void store(Void key, CategoryListDataModel data) {
        data.getCategories().stream()
                .map(categoryDataModel -> transformer.transform(categoryDataModel.getId(), categoryDataModel))
                .forEach(categoryDAO::storeCategory);
    }
}
