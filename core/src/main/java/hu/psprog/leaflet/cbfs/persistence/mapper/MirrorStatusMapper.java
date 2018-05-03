package hu.psprog.leaflet.cbfs.persistence.mapper;

import hu.psprog.leaflet.failover.api.domain.MirrorStatus;
import hu.psprog.leaflet.failover.api.domain.MirrorType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * {@link RowMapper} implementation for {@link MirrorStatus} list.
 *
 * @author Peter Smith
 */
@Component
public class MirrorStatusMapper implements RowMapper<List<MirrorStatus>> {

    private static final String CNT_CATEGORY = "cnt_category";
    private static final String CNT_ENTRY_PAGE = "cnt_entry_page";
    private static final String CNT_ENTRY = "cnt_entry";
    private static final String CNT_DOCUMENT = "cnt_document";

    @Override
    public List<MirrorStatus> mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Arrays.asList(
                createMirrorStatus(MirrorType.CATEGORY, rs.getInt(CNT_CATEGORY)),
                createMirrorStatus(MirrorType.ENTRY_PAGE, rs.getInt(CNT_ENTRY_PAGE)),
                createMirrorStatus(MirrorType.ENTRY, rs.getInt(CNT_ENTRY)),
                createMirrorStatus(MirrorType.DOCUMENT, rs.getInt(CNT_DOCUMENT)));
    }

    private MirrorStatus createMirrorStatus(MirrorType mirrorType, int count) {
        return MirrorStatus.getBuilder()
                .withMirrorType(mirrorType)
                .withNumberOfRecords(count)
                .build();
    }
}
