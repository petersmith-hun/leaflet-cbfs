package hu.psprog.leaflet.cbfs.service.adapter.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import hu.psprog.leaflet.cbfs.persistence.CategoryDAO;
import hu.psprog.leaflet.cbfs.service.adapter.DataAdapter;
import hu.psprog.leaflet.cbfs.service.transformer.impl.CategoryStorageTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Peter Smith
 */
@Component
public class CategoryDataAdapter implements DataAdapter<Void, WrapperBodyDataModel<CategoryListDataModel>> {

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
    public WrapperBodyDataModel<CategoryListDataModel> retrieve(Void byKey) throws CommunicationFailureException {
        return categoryBridgeService.getPublicCategories();
    }

    @Override
    public void store(Void key, WrapperBodyDataModel<CategoryListDataModel> data) {
        data.getBody().getCategories().stream()
                .map(categoryDataModel -> transformer.transform(categoryDataModel.getId(), categoryDataModel))
                .forEach(categoryDAO::storeCategory);
    }
}
