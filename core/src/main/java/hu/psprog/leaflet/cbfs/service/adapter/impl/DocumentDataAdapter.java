package hu.psprog.leaflet.cbfs.service.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.DocumentBridgeService;
import hu.psprog.leaflet.cbfs.persistence.DocumentDAO;
import hu.psprog.leaflet.cbfs.service.adapter.DataAdapter;
import hu.psprog.leaflet.cbfs.service.transformer.impl.DocumentStorageTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Peter Smith
 */
@Component
public class DocumentDataAdapter implements DataAdapter<String, WrapperBodyDataModel<DocumentDataModel>> {

    private DocumentBridgeService documentBridgeService;
    private DocumentStorageTransformer transformer;
    private DocumentDAO documentDAO;

    @Autowired
    public DocumentDataAdapter(DocumentBridgeService documentBridgeService, DocumentStorageTransformer transformer, DocumentDAO documentDAO) {
        this.documentBridgeService = documentBridgeService;
        this.transformer = transformer;
        this.documentDAO = documentDAO;
    }

    @Override
    public WrapperBodyDataModel<DocumentDataModel> retrieve(String byKey) throws CommunicationFailureException {
        return documentBridgeService.getDocumentByLink(byKey);
    }

    @Override
    public void store(String key, WrapperBodyDataModel<DocumentDataModel> data) {
        documentDAO.storeDocument(transformer.transform(key, data));
    }
}
