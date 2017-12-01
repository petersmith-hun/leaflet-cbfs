package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.domain.Category;
import hu.psprog.leaflet.cbfs.persistence.CategoryDAO;
import hu.psprog.leaflet.cbfs.persistence.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link CategoryDAO}.
 *
 * @author Peter Smith
 */
@Repository
public class CategoryDAOImpl implements CategoryDAO {

    private NamedParameterJdbcTemplate failoverJdbcTemplate;
    private QueryRegistry queryRegistry;
    private CategoryMapper categoryMapper;

    @Autowired
    public CategoryDAOImpl(NamedParameterJdbcTemplate failoverJdbcTemplate, QueryRegistry queryRegistry, CategoryMapper categoryMapper) {
        this.failoverJdbcTemplate = failoverJdbcTemplate;
        this.queryRegistry = queryRegistry;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> getCategories() {
        return failoverJdbcTemplate.query(queryRegistry.getCategoriesQuery(), categoryMapper);
    }

    @Override
    public void storeCategory(Category category) {
        failoverJdbcTemplate.execute(queryRegistry.getStoreCategoryQuery(), paramMap(category), QueryRegistry.PREPARED_STATEMENT_CALLBACK);
    }

    @Override
    public void truncate() {
        failoverJdbcTemplate.execute(queryRegistry.getTruncateCategoriesQuery(), QueryRegistry.PREPARED_STATEMENT_CALLBACK);
    }

    private Map<String, Object> paramMap(Category category) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", category.getId());
        paramMap.put("title", category.getTitle());

        return paramMap;
    }
}
