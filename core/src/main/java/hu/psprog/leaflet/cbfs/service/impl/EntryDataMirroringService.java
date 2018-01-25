package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.cbfs.domain.MirrorType;
import hu.psprog.leaflet.cbfs.persistence.EntryPageDAO;
import hu.psprog.leaflet.cbfs.service.DataMirroringService;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.cbfs.service.adapter.impl.EntryDataAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * {@link DataMirroringService} implementation for entry data mirroring.
 *
 * @author Peter Smith
 */
@Service
public class EntryDataMirroringService implements DataMirroringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryDataMirroringService.class);

    private EntryDataAdapter entryDataAdapter;
    private EntryPageDAO entryPageDAO;
    private FailoverStatusService failoverStatusService;

    @Autowired
    public EntryDataMirroringService(EntryDataAdapter entryDataAdapter, EntryPageDAO entryPageDAO, FailoverStatusService failoverStatusService) {
        this.entryDataAdapter = entryDataAdapter;
        this.entryPageDAO = entryPageDAO;
        this.failoverStatusService = failoverStatusService;
    }

    @Override
    public void load() {

        Set<String> links = entryPageDAO.collectAllEntryLinks();
        LOGGER.info("Start collecting entry data for [{}] public entry links", links.size());

        links.forEach(link -> {
            try {
                LOGGER.info("Start collecting entry data for [{}]", link);
                WrapperBodyDataModel<ExtendedEntryDataModel> result = entryDataAdapter.retrieve(link);
                entryDataAdapter.store(link, result);
                LOGGER.info("Collected entry data for [{}]", link);
            } catch (Exception e) {
                LOGGER.error("Failed to retrieve entry data for link [{}].", link, e);
                failoverStatusService.markMirroringFailure(MirrorType.ENTRY);
            }
        });
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
