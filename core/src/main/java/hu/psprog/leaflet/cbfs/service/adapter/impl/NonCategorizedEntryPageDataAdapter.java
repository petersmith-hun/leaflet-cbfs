package hu.psprog.leaflet.cbfs.service.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
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
 * Implementation of {@link DataAdapter} for non-categorized entry pages.
 *  - data source is {@link EntryBridgeService#getPageOfPublicEntries(int, int, OrderBy.Entry, OrderDirection)} method
 *  - paging is always set to default (configured in application.properties)
 *  - data key is an {@link EntryPageKey} instance with only the page number set
 *
 * @author Peter Smith
 */
@Component
public class NonCategorizedEntryPageDataAdapter implements DataAdapter<EntryPageKey, WrapperBodyDataModel<EntryListDataModel>> {

    private MirroringConfiguration mirroringConfiguration;
    private EntryBridgeService entryBridgeService;
    private EntryPageDAO entryPageDAO;
    private EntryPageStorageTransformer transformer;

    @Autowired
    public NonCategorizedEntryPageDataAdapter(MirroringConfiguration mirroringConfiguration, EntryBridgeService entryBridgeService,
                                              EntryPageDAO entryPageDAO, EntryPageStorageTransformer transformer) {
        this.mirroringConfiguration = mirroringConfiguration;
        this.entryBridgeService = entryBridgeService;
        this.entryPageDAO = entryPageDAO;
        this.transformer = transformer;
    }

    @Override
    public WrapperBodyDataModel<EntryListDataModel> retrieve(EntryPageKey byKey) throws CommunicationFailureException {
        return entryBridgeService.getPageOfPublicEntries(byKey.getPage(),
                mirroringConfiguration.getLimit(),
                mirroringConfiguration.getOrderBy(),
                mirroringConfiguration.getOrderDir());
    }

    @Override
    public void store(EntryPageKey key, WrapperBodyDataModel<EntryListDataModel> data) {
        entryPageDAO.storePage(transformer.transform(key, data));
    }
}
