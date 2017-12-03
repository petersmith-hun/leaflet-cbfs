package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import hu.psprog.leaflet.cbfs.persistence.DocumentDAO;
import hu.psprog.leaflet.cbfs.service.snapshot.SnapshotRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link SnapshotRetrievalService} implementation for documents.
 *
 * @author Peter Smith
 */
@Component
public class DocumentSnapshotRetrievalService implements SnapshotRetrievalService<String> {

    private DocumentDAO documentDAO;

    @Autowired
    public DocumentSnapshotRetrievalService(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
    }

    @Override
    public String retrieve(String key) {
        return documentDAO.getByLink(key).getContent();
    }
}
