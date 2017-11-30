package hu.psprog.leaflet.cbfs.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Category data domain.
 * Only ID and title is stored.
 *
 * @author Peter Smith
 */
public class Category {

    private long id;
    private String title;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return new EqualsBuilder()
                .append(id, category.id)
                .append(title, category.title)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .toString();
    }

    public static CategoryBuilder getBuilder() {
        return new CategoryBuilder();
    }

    /**
     * Builder for {@link Category}.
     */
    public static final class CategoryBuilder {
        private long id;
        private String title;

        private CategoryBuilder() {
        }

        public CategoryBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public CategoryBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Category build() {
            Category category = new Category();
            category.id = this.id;
            category.title = this.title;
            return category;
        }
    }
}
