package hu.psprog.leaflet.cbfs.service.transformer.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.cbfs.domain.Entry;
import org.springframework.stereotype.Component;

/**
 * {@link AbstractStorageTransformer} implementation to convert {@link WrapperBodyDataModel<ExtendedEntryDataModel>} object to {@link Entry} domain object.
 * Entry content must be converted to JSON.
 *
 * @author Peter Smith
 */
@Component
public class EntryStorageTransformer extends AbstractStorageTransformer<String, WrapperBodyDataModel<ExtendedEntryDataModel>, Entry> {

    @Override
    public Entry transform(String key, WrapperBodyDataModel<ExtendedEntryDataModel> source) {
        return Entry.getBuilder()
                .withLink(key)
                .withContent(convertToJSON(source))
                .build();
    }
}
