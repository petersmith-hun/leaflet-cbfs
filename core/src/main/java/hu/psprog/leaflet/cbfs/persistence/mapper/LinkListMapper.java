package hu.psprog.leaflet.cbfs.persistence.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps 'link_list' field to {@link List} of {@link String} objects.
 *
 * @author Peter Smith
 */
@Component
public class LinkListMapper implements RowMapper<List<String>> {

    private static final String COLUMN_LINK_LIST = "link_list";
    private static final String LIST_SEPARATOR = ",";

    @Override
    public List<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Arrays.stream(rs.getString(COLUMN_LINK_LIST).split(LIST_SEPARATOR))
                .filter(link -> !link.isEmpty())
                .collect(Collectors.toList());
    }
}
