package hu.psprog.leaflet.cbfs.persistence.mapper;

import hu.psprog.leaflet.cbfs.domain.FailoverStatus;
import hu.psprog.leaflet.cbfs.domain.StatusEntry;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link RowMapper} implementation to read up a {@link StatusEntry}.
 *
 * @author Peter Smith
 */
@Component
public class StatusEntryMapper implements RowMapper<StatusEntry> {

    private static final String CREATED_AT = "created_at";
    private static final String STATUS = "status";
    private static final String PARAMETER = "parameter";

    @Override
    public StatusEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        return StatusEntry.getBuilder()
                .withCreated(rs.getTimestamp(CREATED_AT))
                .withStatus(FailoverStatus.valueOf(rs.getString(STATUS)))
                .withParameter(rs.getString(PARAMETER))
                .build();
    }
}
