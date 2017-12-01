package hu.psprog.leaflet.cbfs.persistence.mapper;

import hu.psprog.leaflet.cbfs.domain.Entry;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link RowMapper} implementation to read up an {@link Entry}.
 *
 * @author Peter Smith
 */
@Component
public class EntryMapper implements RowMapper<Entry> {

    private static final String LINK = "link";
    private static final String CONTENT = "content";

    @Override
    public Entry mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Entry.getBuilder()
                .withLink(rs.getString(LINK))
                .withContent(rs.getString(CONTENT))
                .build();
    }
}
