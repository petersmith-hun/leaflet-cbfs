package hu.psprog.leaflet.cbfs.service.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.cbfs.config.MirroringConfiguration;
import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.persistence.EntryPageDAO;
import hu.psprog.leaflet.cbfs.service.adapter.DataAdapter;
import hu.psprog.leaflet.cbfs.service.transformer.impl.EntryPageStorageTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Peter Smith
 */
@Component
public class CategorizedEntryPageDataAdapter implements DataAdapter<EntryPageKey, WrapperBodyDataModel<EntryListDataModel>> {

    private MirroringConfiguration mirroringConfiguration;
    private EntryBridgeService entryBridgeService;
    private EntryPageDAO entryPageDAO;
    private EntryPageStorageTransformer transformer;

    @Autowired
    public CategorizedEntryPageDataAdapter(MirroringConfiguration mirroringConfiguration, EntryBridgeService entryBridgeService,
                                           EntryPageDAO entryPageDAO, EntryPageStorageTransformer transformer) {
        this.mirroringConfiguration = mirroringConfiguration;
        this.entryBridgeService = entryBridgeService;
        this.entryPageDAO = entryPageDAO;
        this.transformer = transformer;
    }

    @Override
    public WrapperBodyDataModel<EntryListDataModel> retrieve(EntryPageKey byKey) throws CommunicationFailureException {
        return entryBridgeService.getPageOfPublicEntriesByCategory(byKey.getCategoryID(), byKey.getPage(),
                mirroringConfiguration.getLimit(),
                mirroringConfiguration.getOrderBy(),
                mirroringConfiguration.getOrderDir());
    }

    @Override
    public void store(EntryPageKey key, WrapperBodyDataModel<EntryListDataModel> data) {
        entryPageDAO.storePage(transformer.transform(key, data));
    }
}
