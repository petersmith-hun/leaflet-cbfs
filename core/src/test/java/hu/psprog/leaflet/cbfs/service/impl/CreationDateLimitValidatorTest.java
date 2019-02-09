package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    private static final ZonedDateTime CREATION_DATE_LIMIT_AS_DATE = prepareDate(3);

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
                                ? prepareDate(afterLimit ? 4 : 2)
                                : null)
                        .build())
                .build();
    }

    private static ZonedDateTime prepareDate(int day) {
        return ZonedDateTime.of(2018, 2, day, 0, 0, 0, 0, ZoneId.systemDefault());
    }
}