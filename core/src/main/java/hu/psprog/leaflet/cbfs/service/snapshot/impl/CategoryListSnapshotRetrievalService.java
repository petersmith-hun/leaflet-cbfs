package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.cbfs.domain.Category;
import hu.psprog.leaflet.cbfs.persistence.CategoryDAO;
import hu.psprog.leaflet.cbfs.service.snapshot.SnapshotRetrievalService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link SnapshotRetrievalService} for category list.
 *
 * @author Peter Smith
 */
@Service
public class CategoryListSnapshotRetrievalService implements SnapshotRetrievalService<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryListSnapshotRetrievalService.class);

    private static final String BODY = "body";
    private static final String CATEGORIES = "categories";

    private CategoryDAO categoryDAO;
    private ObjectMapper objectMapper;

    @Autowired
    public CategoryListSnapshotRetrievalService(CategoryDAO categoryDAO, ObjectMapper objectMapper) {
        this.categoryDAO = categoryDAO;
        this.objectMapper = objectMapper;
    }

    @Override
    public String retrieve(Void key) {

        Map<String, Map<String, List<Category>>> result = buildWrapper(categoryDAO.getCategories());
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to convert category list to JSON.", e);
        }

        return StringUtils.EMPTY;
    }

    private Map<String, Map<String, List<Category>>> buildWrapper(List<Category> categoryList) {

        Map<String, List<Category>> categoryMap = new HashMap<>();
        categoryMap.put(CATEGORIES, categoryList);

        Map<String, Map<String, List<Category>>> wrapper = new HashMap<>();
        wrapper.put(BODY, categoryMap);

        return wrapper;
    }
}
