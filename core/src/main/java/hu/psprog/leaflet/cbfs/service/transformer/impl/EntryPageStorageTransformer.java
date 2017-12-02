package hu.psprog.leaflet.cbfs.service.transformer.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.cbfs.domain.Entry;
import hu.psprog.leaflet.cbfs.domain.EntryPage;
import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.service.transformer.StorageTransformer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Peter Smith
 */
@Component
public class EntryPageStorageTransformer implements StorageTransformer<EntryPageKey, WrapperBodyDataModel<EntryListDataModel>, EntryPage> {

    @Override
    public EntryPage transform(EntryPageKey key, WrapperBodyDataModel<EntryListDataModel> source) {
        return EntryPage.getBuilder()
                .withPage(key.getPage())
                .withCategoryID(key.getCategoryID())
                .withEntries(buildEntryList(source))
                .build();
    }

    private List<Entry> buildEntryList(WrapperBodyDataModel<EntryListDataModel> sourceList) {
        return sourceList.getBody().getEntries().stream()
                .map(entryDataModel -> Entry.getBuilder()
                        .withLink(entryDataModel.getLink())
                        .build())
                .collect(Collectors.toList());
    }
}
