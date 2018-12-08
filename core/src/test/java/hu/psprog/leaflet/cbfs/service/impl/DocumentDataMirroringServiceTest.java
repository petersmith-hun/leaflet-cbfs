package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentListDataModel;
import hu.psprog.leaflet.api.rest.response.document.EditDocumentDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.DocumentBridgeService;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.cbfs.service.adapter.impl.DocumentDataAdapter;
import hu.psprog.leaflet.failover.api.domain.MirrorType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DocumentDataMirroringService}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentDataMirroringServiceTest {

    private static final WrapperBodyDataModel<DocumentDataModel> DOCUMENT_DATA = WrapperBodyDataModel.getBuilder().build();
    private static final String LINK_1 = "link-1";
    private static final String LINK_2 = "link-2";
    private static final String LINK_3 = "link-3";

    @Mock
    private DocumentDataAdapter documentDataAdapter;

    @Mock
    private DocumentBridgeService documentBridgeService;

    @Mock
    private FailoverStatusService failoverStatusService;

    @InjectMocks
    private DocumentDataMirroringService documentDataMirroringService;

    @Test
    public void shouldLoadDocuments() throws CommunicationFailureException {

        // given
        given(documentBridgeService.getPublicDocuments()).willReturn(prepareDocumentList());
        given(documentDataAdapter.retrieve(anyString())).willReturn(DOCUMENT_DATA);

        // when
        documentDataMirroringService.load();

        // then
        verify(documentBridgeService).getPublicDocuments();
        verify(documentDataAdapter, times(3)).retrieve(anyString());
        verify(documentDataAdapter).store(LINK_1, DOCUMENT_DATA);
        verify(documentDataAdapter).store(LINK_2, DOCUMENT_DATA);
        verify(documentDataAdapter).store(LINK_3, DOCUMENT_DATA);
    }

    @Test
    public void shouldFailSilentlyOnListRetrievalError() throws CommunicationFailureException {


        // given
        doThrow(CommunicationFailureException.class).when(documentBridgeService).getPublicDocuments();

        // when
        documentDataMirroringService.load();

        // then
        // silent fail
        verify(failoverStatusService).markMirroringFailure(MirrorType.DOCUMENT);
    }

    @Test
    public void shouldFailSilentlyOnEntityRetrievalError() throws CommunicationFailureException {


        // given
        given(documentBridgeService.getPublicDocuments()).willReturn(prepareDocumentList());
        doThrow(CommunicationFailureException.class).when(documentDataAdapter).retrieve(any());

        // when
        documentDataMirroringService.load();

        // then
        // silent fail
        verify(failoverStatusService, times(3)).markMirroringFailure(MirrorType.DOCUMENT);
    }

    private DocumentListDataModel prepareDocumentList() {
        return DocumentListDataModel.getBuilder()
                .withItem(prepareDocumentDataModel(LINK_1))
                .withItem(prepareDocumentDataModel(LINK_2))
                .withItem(prepareDocumentDataModel(LINK_3))
                .build();
    }

    private EditDocumentDataModel prepareDocumentDataModel(String link) {
        return EditDocumentDataModel.getExtendedBuilder()
                .withLink(link)
                .build();
    }
}
