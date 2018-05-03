package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.cbfs.service.DataMirroringService;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.cbfs.service.adapter.impl.CategoryDataAdapter;
import hu.psprog.leaflet.failover.api.domain.MirrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link DataMirroringService} implementation for category data mirroring.
 *
 * @author Peter Smith
 */
@Service
public class CategoryDataMirroringService implements DataMirroringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDataMirroringService.class);

    private CategoryDataAdapter categoryDataAdapter;
    private FailoverStatusService failoverStatusService;

    @Autowired
    public CategoryDataMirroringService(CategoryDataAdapter categoryDataAdapter, FailoverStatusService failoverStatusService) {
        this.categoryDataAdapter = categoryDataAdapter;
        this.failoverStatusService = failoverStatusService;
    }

    @Override
    public void load() {
        try {
            LOGGER.info("Start collecting categories...");
            CategoryListDataModel result = categoryDataAdapter.retrieve(null);
            categoryDataAdapter.store(null, result);
            LOGGER.info("Collecting categories done.");
        } catch (Exception e) {
            LOGGER.error("Failed to collect category data.", e);
            failoverStatusService.markMirroringFailure(MirrorType.CATEGORY);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
