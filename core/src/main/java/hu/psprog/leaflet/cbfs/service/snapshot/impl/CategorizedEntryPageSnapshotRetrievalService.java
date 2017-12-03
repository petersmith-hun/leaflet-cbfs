package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.persistence.EntryPageDAO;
import hu.psprog.leaflet.cbfs.service.snapshot.SnapshotRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link SnapshotRetrievalService} implementation for categorized entry pages.
 *
 * @author Peter Smith
 */
@Service
public class CategorizedEntryPageSnapshotRetrievalService implements SnapshotRetrievalService<EntryPageKey> {

    private EntryPageDAO entryPageDAO;

    @Autowired
    public CategorizedEntryPageSnapshotRetrievalService(EntryPageDAO entryPageDAO) {
        this.entryPageDAO = entryPageDAO;
    }

    @Override
    public String retrieve(EntryPageKey key) {
        return null;
    }
}
