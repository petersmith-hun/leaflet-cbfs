package hu.psprog.leaflet.cbfs.service.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.cbfs.config.MirroringConfiguration;
import hu.psprog.leaflet.cbfs.domain.EntryPage;
import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.persistence.EntryPageDAO;
import hu.psprog.leaflet.cbfs.service.transformer.impl.EntryPageStorageTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CategorizedEntryPageDataAdapter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CategorizedEntryPageDataAdapterTest {

    private static final int PAGE_NUMBER = 2;
    private static final Long CATEGORY_ID = 3L;
    private static final int LIMIT = 10;
    private static final OrderBy.Entry ORDER_BY = OrderBy.Entry.CREATED;
    private static final OrderDirection ORDER_DIRECTION = OrderDirection.DESC;

    private static final EntryPageKey ENTRY_PAGE_KEY = EntryPageKey.build(PAGE_NUMBER, CATEGORY_ID);
    private static final WrapperBodyDataModel<EntryListDataModel> RESULT = WrapperBodyDataModel.getBuilder().build();
    private static final EntryPage ENTRY_PAGE = EntryPage.getBuilder().build();

    @Mock
    private MirroringConfiguration mirroringConfiguration;

    @Mock
    private EntryBridgeService entryBridgeService;

    @Mock
    private EntryPageDAO entryPageDAO;

    @Mock
    private EntryPageStorageTransformer entryPageStorageTransformer;

    @InjectMocks
    private CategorizedEntryPageDataAdapter categorizedEntryPageDataAdapter;

    @Before
    public void setup() {
        given(mirroringConfiguration.getLimit()).willReturn(LIMIT);
        given(mirroringConfiguration.getOrderBy()).willReturn(ORDER_BY);
        given(mirroringConfiguration.getOrderDir()).willReturn(ORDER_DIRECTION);
        given(entryPageStorageTransformer.transform(ENTRY_PAGE_KEY, RESULT)).willReturn(ENTRY_PAGE);
    }

    @Test
    public void shouldRetrieve() throws CommunicationFailureException {

        // when
        categorizedEntryPageDataAdapter.retrieve(ENTRY_PAGE_KEY);

        // then
        verify(entryBridgeService).getPageOfPublicEntriesByCategory(CATEGORY_ID, PAGE_NUMBER, LIMIT, ORDER_BY, ORDER_DIRECTION);
    }

    @Test
    public void shouldStore() {

        // when
        categorizedEntryPageDataAdapter.store(ENTRY_PAGE_KEY, RESULT);

        // then
        verify(entryPageDAO).storePage(ENTRY_PAGE);
    }
}
