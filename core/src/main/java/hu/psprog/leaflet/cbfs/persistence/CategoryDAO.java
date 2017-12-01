package hu.psprog.leaflet.cbfs.persistence;

import hu.psprog.leaflet.cbfs.domain.Category;

import java.util.List;

/**
 * {@link Category} related DAO operations.
 *
 * @author Peter Smith
 */
public interface CategoryDAO {

    /**
     * Returns {@link List} of {@link Category} objects.
     *
     * @return list of categories
     */
    List<Category> getCategories();

    /**
     * Stores given {@link Category}.
     *
     * @param category {@link Category} object to store
     */
    void storeCategory(Category category);

    /**
     * Truncates table of {@link Category} objects.
     */
    void truncate();
}
