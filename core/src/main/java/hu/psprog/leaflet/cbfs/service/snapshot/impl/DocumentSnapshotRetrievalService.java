package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import hu.psprog.leaflet.cbfs.domain.Document;
import hu.psprog.leaflet.cbfs.persistence.DocumentDAO;
import hu.psprog.leaflet.cbfs.service.snapshot.SnapshotRetrievalService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        return Optional.ofNullable(documentDAO.getByLink(key))
                .map(Document::getContent)
                .orElse(StringUtils.EMPTY);
    }
}
