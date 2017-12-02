package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentListDataModel;
import hu.psprog.leaflet.bridge.service.DocumentBridgeService;
import hu.psprog.leaflet.cbfs.service.DataMirroringService;
import hu.psprog.leaflet.cbfs.service.adapter.impl.DocumentDataAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Peter Smith
 */
@Service
public class DocumentDataMirroringService implements DataMirroringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentDataMirroringService.class);

    private DocumentDataAdapter documentDataAdapter;
    private DocumentBridgeService documentBridgeService;

    @Autowired
    public DocumentDataMirroringService(DocumentDataAdapter documentDataAdapter, DocumentBridgeService documentBridgeService) {
        this.documentDataAdapter = documentDataAdapter;
        this.documentBridgeService = documentBridgeService;
    }

    @Override
    public void load() {

        try {
            LOGGER.info("Start collecting document data...");
            DocumentListDataModel documentList = documentBridgeService.getPublicDocuments();
            LOGGER.info("Start collecting document data for [{}] documents.", documentList.getDocuments().size());
            documentList.getDocuments().forEach(document -> {
                try {
                    WrapperBodyDataModel<DocumentDataModel> result = documentDataAdapter.retrieve(document.getLink());
                    documentDataAdapter.store(document.getLink(), result);
                } catch (Exception e) {
                    LOGGER.error("Failed to retrieve document data for [{}].", document.getLink(), e);
                }
            });
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve document list.", e);
        }
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
