package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link CreationDateLimitValidator}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CreationDateLimitValidatorTest {

    private static final String CREATION_DATE_LIMIT = "2018-02-03";
    private static final DateFormat DEFAULT_DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.getDefault());
    private static final Date CREATION_DATE_LIMIT_AS_DATE = prepareDate(3);

    private CreationDateLimitValidator creationDateLimitValidator;

    @Test
    public void shouldInitiateWithLimit() {

        // when
        creationDateLimitValidator = new CreationDateLimitValidator(CREATION_DATE_LIMIT);

        // then
        assertThat(creationDateLimitValidator.getCreationDateLimit(), equalTo(CREATION_DATE_LIMIT_AS_DATE));
    }

    @Test
    public void shouldInitiateWithoutLimitForEmptyString() {

        // when
        creationDateLimitValidator = new CreationDateLimitValidator(StringUtils.EMPTY);

        // then
        assertThat(creationDateLimitValidator.getCreationDateLimit(), nullValue());
    }

    @Test
    public void shouldInitiateWithoutLimitForNullString() {

        // when
        creationDateLimitValidator = new CreationDateLimitValidator(null);

        // then
        assertThat(creationDateLimitValidator.getCreationDateLimit(), nullValue());
    }

    @Test
    public void shouldInitiateWithoutLimitForInvalidDateString() {

        // when
        creationDateLimitValidator = new CreationDateLimitValidator("invalid-date");

        // then
        assertThat(creationDateLimitValidator.getCreationDateLimit(), nullValue());
    }

    @Test
    public void shouldBeValidForCreationDateAfterLimit() {

        // given
        creationDateLimitValidator = new CreationDateLimitValidator(CREATION_DATE_LIMIT);

        // when
        boolean result = creationDateLimitValidator.isValid(prepareDataModel(true, true));

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldBeInvalidForCreationDateBeforeLimit() {

        // given
        creationDateLimitValidator = new CreationDateLimitValidator(CREATION_DATE_LIMIT);

        // when
        boolean result = creationDateLimitValidator.isValid(prepareDataModel(true, false));

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldBeValidForTurnedOffValidation() {

        // given
        creationDateLimitValidator = new CreationDateLimitValidator(null);

        // when
        boolean result = creationDateLimitValidator.isValid(prepareDataModel(true, true));

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldBeValidForUnParsableCreationDate() {

        // given
        creationDateLimitValidator = new CreationDateLimitValidator(CREATION_DATE_LIMIT);

        // when
        boolean result = creationDateLimitValidator.isValid(prepareDataModel(false, true));

        // then
        assertThat(result, is(true));
    }

    private WrapperBodyDataModel<ExtendedEntryDataModel> prepareDataModel(boolean parsableDate, boolean afterLimit) {
        return WrapperBodyDataModel.getBuilder()
                .withBody(ExtendedEntryDataModel.getExtendedBuilder()
                        .withCreated(parsableDate
                                ? DEFAULT_DATE_FORMAT.format(prepareDate(afterLimit ? 4 : 2))
                                : "invalid-date")
                        .build())
                .build();
    }

    private static Date prepareDate(int day) {
        return new Calendar.Builder()
                .setDate(2018, 1, day)
                .build()
                .getTime();
    }
}