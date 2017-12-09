package hu.psprog.leaflet.cbfs.service.transformer.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.cbfs.domain.EntryPage;
import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.service.transformer.StorageTransformer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link StorageTransformer} implementation to convert {@link WrapperBodyDataModel<EntryListDataModel>} object to {@link EntryPage} domain object.
 * Key type is {@link EntryPageKey} because of the complex key (page number and category ID).
 *
 * @author Peter Smith
 */
@Component
public class EntryPageStorageTransformer extends AbstractStorageTransformer<EntryPageKey, WrapperBodyDataModel<EntryListDataModel>, EntryPage> {

    @Override
    public EntryPage transform(EntryPageKey key, WrapperBodyDataModel<EntryListDataModel> source) {
        return EntryPage.getBuilder()
                .withPage(key.getPage())
                .withCategoryID(key.getCategoryID())
                .withEntries(buildEntryList(source))
                .withContent(convertToJSON(source))
                .build();
    }

    private List<String> buildEntryList(WrapperBodyDataModel<EntryListDataModel> sourceList) {
        return sourceList.getBody().getEntries().stream()
                .map(EntryDataModel::getLink)
                .collect(Collectors.toList());
    }
}
