package hu.psprog.leaflet.cbfs.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Peter Smith
 */
public class EntryPageKey {

    private int page;
    private Long categoryID;

    private EntryPageKey(int page) {
        this.page = page;
    }

    private EntryPageKey(int page, Long categoryID) {
        this.page = page;
        this.categoryID = categoryID;
    }

    public int getPage() {
        return page;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public static EntryPageKey build(int page) {
        return new EntryPageKey(page);
    }

    public static EntryPageKey build(int page, Long categoryID) {
        return new EntryPageKey(page, categoryID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EntryPageKey that = (EntryPageKey) o;

        return new EqualsBuilder()
                .append(page, that.page)
                .append(categoryID, that.categoryID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(page)
                .append(categoryID)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("page", page)
                .append("categoryID", categoryID)
                .toString();
    }
}
