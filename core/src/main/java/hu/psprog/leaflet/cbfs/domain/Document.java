package hu.psprog.leaflet.cbfs.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Document data identified by their link.
 * Content is in JSON to prevent unnecessary conversion.
 *
 * @author Peter Smith
 */
public class Document {

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

        Document document = (Document) o;

        return new EqualsBuilder()
                .append(link, document.link)
                .append(content, document.content)
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

    public static DocumentBuilder getBuilder() {
        return new DocumentBuilder();
    }

    /**
     * Builder for {@link Document}.
     */
    public static final class DocumentBuilder {
        private String link;
        private String content;

        private DocumentBuilder() {
        }

        public DocumentBuilder withLink(String link) {
            this.link = link;
            return this;
        }

        public DocumentBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public Document build() {
            Document document = new Document();
            document.content = this.content;
            document.link = this.link;
            return document;
        }
    }
}
