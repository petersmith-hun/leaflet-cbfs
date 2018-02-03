package hu.psprog.leaflet.cbfs.service.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.cbfs.persistence.EntryDAO;
import hu.psprog.leaflet.cbfs.service.adapter.DataAdapter;
import hu.psprog.leaflet.cbfs.service.impl.CreationDateLimitValidator;
import hu.psprog.leaflet.cbfs.service.transformer.impl.EntryStorageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link DataAdapter} for entries.
 *  - data source is {@link EntryBridgeService#getEntryByLink(String)} method
 *  - data key is the link of the entry as String
 *
 * @author Peter Smith
 */
@Component
public class EntryDataAdapter implements DataAdapter<String, WrapperBodyDataModel<ExtendedEntryDataModel>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryDataAdapter.class);

    private EntryBridgeService entryBridgeService;
    private EntryStorageTransformer transformer;
    private EntryDAO entryDAO;
    private CreationDateLimitValidator creationDateLimitValidator;

    @Autowired
    public EntryDataAdapter(EntryBridgeService entryBridgeService, EntryStorageTransformer transformer,
                            EntryDAO entryDAO, CreationDateLimitValidator creationDateLimitValidator) {
        this.entryBridgeService = entryBridgeService;
        this.transformer = transformer;
        this.entryDAO = entryDAO;
        this.creationDateLimitValidator = creationDateLimitValidator;
    }

    @Override
    public WrapperBodyDataModel<ExtendedEntryDataModel> retrieve(String byKey) throws CommunicationFailureException {
        return entryBridgeService.getEntryByLink(byKey);
    }

    @Override
    public void store(String key, WrapperBodyDataModel<ExtendedEntryDataModel> data) {
        if (creationDateLimitValidator.isValid(data)) {
            entryDAO.storeEntry(transformer.transform(key, data));
        } else {
            LOGGER.warn("Entry [{}] dropped by creation date limit ({})", data.getBody().getLink(), creationDateLimitValidator.getCreationDateLimit());
        }
    }
}
