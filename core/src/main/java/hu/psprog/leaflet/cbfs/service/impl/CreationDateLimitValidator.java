package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Validator for creation date limit of entries.
 * Entries created after given limit date will be dropped during mirroring.
 * Limitation rule can be turned off by specifying null or empty string as value.
 *
 * @author Peter Smith
 */
@Component
public class CreationDateLimitValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreationDateLimitValidator.class);

    private static final DateFormat LIMIT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat DEFAULT_DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.getDefault());

    private Date creationDateLimit;

    @Autowired
    public CreationDateLimitValidator(@Value("${mirroring.entries.latest}") String creationDateLimit) {
        try {
            if (Objects.nonNull(creationDateLimit) && !StringUtils.EMPTY.equals(creationDateLimit)) {
                this.creationDateLimit = LIMIT_DATE_FORMAT.parse(creationDateLimit);
                LOGGER.warn("Entries created after {} will be dropped during mirroring", this.creationDateLimit);
            }
        } catch (ParseException e) {
            LOGGER.warn("Limit date {} provided in invalid format - limitation will be turned off", creationDateLimit);
        }
    }

    /**
     * Checks whether given entry should be dropped or not by its creation date based on set limit date.
     *
     * @param data raw entry data
     * @return {@code true} if entry can be processed, {@code false} if it should be dropped
     */
    public boolean isValid(WrapperBodyDataModel<ExtendedEntryDataModel> data) {

        boolean applicable = true;
        if (Objects.nonNull(creationDateLimit)) {
            try {
                Date creationDate = DEFAULT_DATE_FORMAT.parse(data.getBody().getCreated());
                applicable = creationDate.after(creationDateLimit);
            } catch (ParseException e) {
                LOGGER.error("Failed to parse creation date [{}] of entry [{}] - returning successful validation", data.getBody().getCreated(), data.getBody().getLink());
            }
        }

        return applicable;
    }

    /**
     * Returns currently set creation date limit.
     *
     * @return currently set creation date limit
     */
    public Date getCreationDateLimit() {
        return creationDateLimit;
    }
}
