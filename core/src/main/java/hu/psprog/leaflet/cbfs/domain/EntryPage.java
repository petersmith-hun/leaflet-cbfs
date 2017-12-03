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

    private int page;
    private Long categoryID;
    private List<String> entries;
    private String content;

    public Long getCategoryID() {
        return categoryID;
    }

    public int getPage() {
        return page;
    }

    public List<String> getEntries() {
        return entries;
    }

    public String getContent() {
        return content;
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
                .append(content, entryPage.content)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(categoryID)
                .append(page)
                .append(entries)
                .append(content)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("categoryID", categoryID)
                .append("page", page)
                .append("entries", entries)
                .append("content", content)
                .toString();
    }

    public static EntryPageBuilder getBuilder() {
        return new EntryPageBuilder();
    }

    /**
     * Builder for {@link EntryPage}.
     */
    public static final class EntryPageBuilder {
        private int page;
        private Long categoryID;
        private List<String> entries;
        private String content;

        private EntryPageBuilder() {
        }

        public EntryPageBuilder withCategoryID(Long categoryID) {
            this.categoryID = categoryID;
            return this;
        }

        public EntryPageBuilder withPage(int page) {
            this.page = page;
            return this;
        }

        public EntryPageBuilder withEntries(List<String> entries) {
            this.entries = entries;
            return this;
        }

        public EntryPageBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public EntryPage build() {
            EntryPage entryPage = new EntryPage();
            entryPage.entries = this.entries;
            entryPage.categoryID = this.categoryID;
            entryPage.page = this.page;
            entryPage.content = this.content;
            return entryPage;
        }
    }
}
