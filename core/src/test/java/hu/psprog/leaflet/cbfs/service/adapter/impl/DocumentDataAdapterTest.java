package hu.psprog.leaflet.cbfs.service.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.DocumentBridgeService;
import hu.psprog.leaflet.cbfs.domain.Document;
import hu.psprog.leaflet.cbfs.persistence.DocumentDAO;
import hu.psprog.leaflet.cbfs.service.transformer.impl.DocumentStorageTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DocumentDataAdapter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentDataAdapterTest {

    private static final String LINK = "link-1";
    private static final WrapperBodyDataModel<DocumentDataModel> RESULT = WrapperBodyDataModel.getBuilder().build();
    private static final Document DOCUMENT = Document.getBuilder().build();

    @Mock
    private DocumentBridgeService documentBridgeService;

    @Mock
    private DocumentStorageTransformer documentStorageTransformer;

    @Mock
    private DocumentDAO documentDAO;

    @InjectMocks
    private DocumentDataAdapter documentDataAdapter;

    @Before
    public void setup() {
        given(documentStorageTransformer.transform(LINK, RESULT)).willReturn(DOCUMENT);
    }

    @Test
    public void shouldRetrieve() throws CommunicationFailureException {

        // when
        documentDataAdapter.retrieve(LINK);

        // then
        verify(documentBridgeService).getDocumentByLink(LINK);
    }

    @Test
    public void shouldStore() {

        // when
        documentDataAdapter.store(LINK, RESULT);

        // then
        verify(documentDAO).storeDocument(DOCUMENT);
    }
}
