package hu.psprog.leaflet.cbfs.persistence.mapper;

import hu.psprog.leaflet.cbfs.domain.Category;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link RowMapper} implementation to read up a {@link Category}.
 *
 * @author Peter Smith
 */
@Component
public class CategoryMapper implements RowMapper<Category> {

    private static final String TITLE = "title";
    private static final String ID = "id";

    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Category.getBuilder()
                .withId(rs.getLong(ID))
                .withTitle(rs.getString(TITLE))
                .build();
    }
}
