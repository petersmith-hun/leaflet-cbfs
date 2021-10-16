package hu.psprog.leaflet.cbfs.service.transformer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.cbfs.domain.Entry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link EntryStorageTransformer}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class EntryStorageTransformerTest {

    private static final String KEY = "transformer";
    private static final WrapperBodyDataModel<ExtendedEntryDataModel> SOURCE = WrapperBodyDataModel.getBuilder()
            .withBody(ExtendedEntryDataModel.getExtendedBuilder().build())
            .build();
    private static final String CONVERTER_BODY = "{...json...}";

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EntryStorageTransformer entryStorageTransformer;

    @Test
    public void shouldTransform() throws JsonProcessingException {

        // given
        given(objectMapper.writeValueAsString(SOURCE)).willReturn(CONVERTER_BODY);

        // when
        Entry result = entryStorageTransformer.transform(KEY, SOURCE);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getLink(), equalTo(KEY));
        assertThat(result.getContent(), equalTo(CONVERTER_BODY));
    }

    @Test
    public void shouldTransformReturnEmptyStringOnJsonConversionFailure() throws JsonProcessingException {

        // given
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(SOURCE);

        // when
        Entry result = entryStorageTransformer.transform(KEY, SOURCE);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getLink(), equalTo(KEY));
        assertThat(result.getContent(), nullValue());
    }
}
