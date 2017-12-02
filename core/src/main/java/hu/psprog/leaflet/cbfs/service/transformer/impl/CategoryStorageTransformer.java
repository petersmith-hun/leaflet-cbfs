package hu.psprog.leaflet.cbfs.service.transformer.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.cbfs.domain.Category;
import hu.psprog.leaflet.cbfs.service.transformer.StorageTransformer;
import org.springframework.stereotype.Component;

/**
 * @author Peter Smith
 */
@Component
public class CategoryStorageTransformer implements StorageTransformer<Long, CategoryDataModel, Category> {

    @Override
    public Category transform(Long key, CategoryDataModel source) {
        return Category.getBuilder()
                .withId(key)
                .withTitle(source.getTitle())
                .build();
    }
}
