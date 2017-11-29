package hu.psprog.leaflet.cbfs.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * 'Page' of entries object.
 * A 'page' can be the list of entries related to given
 *  - page number: paged list without filtering to category
 *  - page number and category ID: page list with category filtering
 *
 * @author Peter Smith
 */
public class EntryPage {

    private long categoryID;
    private int page;
    private List<Entry> entries;

    public long getCategoryID() {
        return categoryID;
    }

    public int getPage() {
        return page;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EntryPage entryPage = (EntryPage) o;

        return new EqualsBuilder()
                .append(categoryID, entryPage.categoryID)
                .append(page, entryPage.page)
                .append(entries, entryPage.entries)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(categoryID)
                .append(page)
                .append(entries)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("categoryID", categoryID)
                .append("page", page)
                .append("entries", entries)
                .toString();
    }

    public static EntryPageBuilder getBuilder() {
        return new EntryPageBuilder();
    }

    /**
     * Builder for {@link EntryPage}.
     */
    public static final class EntryPageBuilder {
        private long categoryID;
        private int page;
        private List<Entry> entries;

        private EntryPageBuilder() {
        }

        public EntryPageBuilder withCategoryID(long categoryID) {
            this.categoryID = categoryID;
            return this;
        }

        public EntryPageBuilder withPage(int page) {
            this.page = page;
            return this;
        }

        public EntryPageBuilder withEntries(List<Entry> entries) {
            this.entries = entries;
            return this;
        }

        public EntryPage build() {
            EntryPage entryPage = new EntryPage();
            entryPage.entries = this.entries;
            entryPage.categoryID = this.categoryID;
            entryPage.page = this.page;
            return entryPage;
        }
    }
}
