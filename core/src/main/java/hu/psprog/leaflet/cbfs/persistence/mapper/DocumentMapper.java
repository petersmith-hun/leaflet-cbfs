package hu.psprog.leaflet.cbfs.persistence.mapper;

import hu.psprog.leaflet.cbfs.domain.Document;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link RowMapper} implementation to read up a {@link Document}.
 *
 * @author Peter Smith
 */
@Component
public class DocumentMapper implements RowMapper<Document> {

    private static final String LINK = "link";
    private static final String CONTENT = "content";

    @Override
    public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Document.getBuilder()
                .withLink(rs.getString(LINK))
                .withContent(rs.getString(CONTENT))
                .build();
    }
}
