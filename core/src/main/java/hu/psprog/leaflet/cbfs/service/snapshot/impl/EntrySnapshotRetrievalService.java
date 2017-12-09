package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import hu.psprog.leaflet.cbfs.domain.Entry;
import hu.psprog.leaflet.cbfs.persistence.EntryDAO;
import hu.psprog.leaflet.cbfs.service.snapshot.SnapshotRetrievalService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return Optional.ofNullable(entryDAO.getByLink(key))
                .map(Entry::getContent)
                .orElse(StringUtils.EMPTY);
    }
}
