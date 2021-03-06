package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.cbfs.config.MirroringConfiguration;
import hu.psprog.leaflet.cbfs.domain.Category;
import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.persistence.CategoryDAO;
import hu.psprog.leaflet.cbfs.service.DataMirroringService;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.cbfs.service.adapter.DataAdapter;
import hu.psprog.leaflet.cbfs.service.adapter.impl.CategorizedEntryPageDataAdapter;
import hu.psprog.leaflet.cbfs.service.adapter.impl.NonCategorizedEntryPageDataAdapter;
import hu.psprog.leaflet.failover.api.domain.MirrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link DataMirroringService} implementation for entry page data mirroring.
 *
 * @author Peter Smith
 */
@Service
public class EntryPageDataMirroringService implements DataMirroringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryPageDataMirroringService.class);

    private MirroringConfiguration mirroringConfiguration;
    private NonCategorizedEntryPageDataAdapter nonCategorizedEntryPageDataAdapter;
    private CategorizedEntryPageDataAdapter categorizedEntryPageDataAdapter;
    private CategoryDAO categoryDAO;
    private FailoverStatusService failoverStatusService;

    @Value("${mirroring.pages.max}")
    private int maxPages;

    @Autowired
    public EntryPageDataMirroringService(MirroringConfiguration mirroringConfiguration, NonCategorizedEntryPageDataAdapter nonCategorizedEntryPageDataAdapter,
                                         CategorizedEntryPageDataAdapter categorizedEntryPageDataAdapter, CategoryDAO categoryDAO, FailoverStatusService failoverStatusService) {
        this.mirroringConfiguration = mirroringConfiguration;
        this.nonCategorizedEntryPageDataAdapter = nonCategorizedEntryPageDataAdapter;
        this.categorizedEntryPageDataAdapter = categorizedEntryPageDataAdapter;
        this.categoryDAO = categoryDAO;
        this.failoverStatusService = failoverStatusService;
    }

    @Override
    public void load() {

        try {
            LOGGER.info("Start collecting non-categorized pages...");
            run(nonCategorizedEntryPageDataAdapter, Collections.singletonList(null));
            LOGGER.info("Collecting non-categorized pages done.");
            LOGGER.info("Start collecting categorized pages...");
            run(categorizedEntryPageDataAdapter, getCategories());
            LOGGER.info("Collecting categorized pages done.");
        } catch (Exception e) {
            LOGGER.error("Failed to load paged data.", e);
            failoverStatusService.markMirroringFailure(MirrorType.ENTRY_PAGE);
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }

    private void run(DataAdapter<EntryPageKey, WrapperBodyDataModel<EntryListDataModel>> dataAdapter, List<Long> categoryIDList) throws CommunicationFailureException {

        WrapperBodyDataModel<EntryListDataModel> dataPage;
        for (Long categoryID : categoryIDList) {
            boolean hasNextPage = true;
            for (int page = mirroringConfiguration.getFirstPage(); hasNextPage; page++, hasNextPage = dataPage.getPagination().isHasNext()) {

                EntryPageKey pageKey = EntryPageKey.build(page, categoryID);
                dataPage = dataAdapter.retrieve(pageKey);
                dataAdapter.store(pageKey, dataPage);
                LOGGER.info("Collected page [{}] for category [{}] with [{}] entries on page", page, categoryID, dataPage.getBody().getEntries().size());

                if (page >= maxPages) {
                    LOGGER.warn("Iteration reached max pages ({}). Breaking loop.", page);
                    break;
                }
            }
        }
    }

    private List<Long> getCategories() {
        return categoryDAO.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList());
    }
}
