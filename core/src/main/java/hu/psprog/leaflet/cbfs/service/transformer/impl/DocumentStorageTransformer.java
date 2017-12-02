package hu.psprog.leaflet.cbfs.service.transformer.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.cbfs.domain.Document;
import org.springframework.stereotype.Component;

/**
 * @author Peter Smith
 */
@Component
public class DocumentStorageTransformer extends AbstractStorageTransformer<String, WrapperBodyDataModel<DocumentDataModel>, Document> {

    @Override
    public Document transform(String key, WrapperBodyDataModel<DocumentDataModel> source) {
        return Document.getBuilder()
                .withLink(key)
                .withContent(convertToJSON(source))
                .build();
    }
}
