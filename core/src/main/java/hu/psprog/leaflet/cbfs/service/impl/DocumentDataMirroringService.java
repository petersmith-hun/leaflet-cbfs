package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.api.rest.response.document.EditDocumentDataModel;
import hu.psprog.leaflet.bridge.service.DocumentBridgeService;
import hu.psprog.leaflet.cbfs.service.DataMirroringService;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.cbfs.service.adapter.impl.DocumentDataAdapter;
import hu.psprog.leaflet.failover.api.domain.MirrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@link DataMirroringService} implementation for document data mirroring.
 *
 * @author Peter Smith
 */
@Service
public class DocumentDataMirroringService implements DataMirroringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentDataMirroringService.class);

    private DocumentDataAdapter documentDataAdapter;
    private DocumentBridgeService documentBridgeService;
    private FailoverStatusService failoverStatusService;

    @Autowired
    public DocumentDataMirroringService(DocumentDataAdapter documentDataAdapter, DocumentBridgeService documentBridgeService,
                                        FailoverStatusService failoverStatusService) {
        this.documentDataAdapter = documentDataAdapter;
        this.documentBridgeService = documentBridgeService;
        this.failoverStatusService = failoverStatusService;
    }

    @Override
    public void load() {

        try {
            LOGGER.info("Start collecting document data...");
            List<EditDocumentDataModel> documentList = documentBridgeService.getPublicDocuments().getDocuments();
            LOGGER.info("Start collecting document data for [{}] documents.", documentList.size());
            documentList.forEach(document -> {
                try {
                    WrapperBodyDataModel<DocumentDataModel> result = documentDataAdapter.retrieve(document.getLink());
                    documentDataAdapter.store(document.getLink(), result);
                } catch (Exception e) {
                    LOGGER.error("Failed to retrieve document data for [{}].", document.getLink(), e);
                    failoverStatusService.markMirroringFailure(MirrorType.DOCUMENT);
                }
            });
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve document list.", e);
            failoverStatusService.markMirroringFailure(MirrorType.DOCUMENT);
        }
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
