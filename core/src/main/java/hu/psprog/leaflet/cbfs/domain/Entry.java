package hu.psprog.leaflet.cbfs.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Entry data identified by their link.
 * Content is in JSON to prevent unnecessary conversion.
 *
 * @author Peter Smith
 */
public class Entry {

    private String link;
    private String content;

    public String getLink() {
        return link;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Entry entry = (Entry) o;

        return new EqualsBuilder()
                .append(link, entry.link)
                .append(content, entry.content)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(link)
                .append(content)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("link", link)
                .append("content", content)
                .toString();
    }

    public static EntryBuilder getBuilder() {
        return new EntryBuilder();
    }

    /**
     * Builder for {@link Entry}.
     */
    public static final class EntryBuilder {
        private String link;
        private String content;

        private EntryBuilder() {
        }

        public EntryBuilder withLink(String link) {
            this.link = link;
            return this;
        }

        public EntryBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public Entry build() {
            Entry entry = new Entry();
            entry.content = this.content;
            entry.link = this.link;
            return entry;
        }
    }
}
