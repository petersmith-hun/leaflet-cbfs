package hu.psprog.leaflet.cbfs.service.transformer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.cbfs.domain.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link DocumentStorageTransformer}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentStorageTransformerTest {

    private static final String KEY = "transformer";
    private static final WrapperBodyDataModel<DocumentDataModel> SOURCE = WrapperBodyDataModel.getBuilder()
            .withBody(DocumentDataModel.getBuilder().build())
            .build();
    private static final String CONVERTER_BODY = "{...json...}";

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DocumentStorageTransformer documentStorageTransformer;

    @Test
    public void shouldTransform() throws JsonProcessingException {

        // given
        given(objectMapper.writeValueAsString(SOURCE)).willReturn(CONVERTER_BODY);

        // when
        Document result = documentStorageTransformer.transform(KEY, SOURCE);

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
        Document result = documentStorageTransformer.transform(KEY, SOURCE);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getLink(), equalTo(KEY));
        assertThat(result.getContent(), nullValue());
    }
}
