package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import hu.psprog.leaflet.cbfs.persistence.EntryDAO;
import hu.psprog.leaflet.cbfs.service.snapshot.SnapshotRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link SnapshotRetrievalService} implementation for entries.
 *
 * @author Peter Smith
 */
@Service
public class EntrySnapshotRetrievalService implements SnapshotRetrievalService<String> {

    private EntryDAO entryDAO;

    @Autowired
    public EntrySnapshotRetrievalService(EntryDAO entryDAO) {
        this.entryDAO = entryDAO;
    }

    @Override
    public String retrieve(String key) {
        return entryDAO.getByLink(key).getContent();
    }
}
