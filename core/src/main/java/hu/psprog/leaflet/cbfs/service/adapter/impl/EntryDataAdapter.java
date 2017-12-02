package hu.psprog.leaflet.cbfs.service.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.cbfs.persistence.EntryDAO;
import hu.psprog.leaflet.cbfs.service.adapter.DataAdapter;
import hu.psprog.leaflet.cbfs.service.transformer.impl.EntryStorageTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Peter Smith
 */
@Component
public class EntryDataAdapter implements DataAdapter<String, WrapperBodyDataModel<ExtendedEntryDataModel>> {

    private EntryBridgeService entryBridgeService;
    private EntryStorageTransformer transformer;
    private EntryDAO entryDAO;

    @Autowired
    public EntryDataAdapter(EntryBridgeService entryBridgeService, EntryStorageTransformer transformer, EntryDAO entryDAO) {
        this.entryBridgeService = entryBridgeService;
        this.transformer = transformer;
        this.entryDAO = entryDAO;
    }

    @Override
    public WrapperBodyDataModel<ExtendedEntryDataModel> retrieve(String byKey) throws CommunicationFailureException {
        return entryBridgeService.getEntryByLink(byKey);
    }

    @Override
    public void store(String key, WrapperBodyDataModel<ExtendedEntryDataModel> data) {
        entryDAO.storeEntry(transformer.transform(key, data));
    }
}
