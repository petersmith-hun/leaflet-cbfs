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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link NonCategorizedEntryPageDataAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class NonCategorizedEntryPageDataAdapterTest {

    private static final int PAGE_NUMBER = 2;
    private static final int LIMIT = 10;
    private static final OrderBy.Entry ORDER_BY = OrderBy.Entry.CREATED;
    private static final OrderDirection ORDER_DIRECTION = OrderDirection.DESC;

    private static final EntryPageKey ENTRY_PAGE_KEY = EntryPageKey.build(PAGE_NUMBER);
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
    private NonCategorizedEntryPageDataAdapter nonCategorizedEntryPageDataAdapter;

    @Test
    public void shouldRetrieve() throws CommunicationFailureException {

        // given
        given(mirroringConfiguration.getLimit()).willReturn(LIMIT);
        given(mirroringConfiguration.getOrderBy()).willReturn(ORDER_BY);
        given(mirroringConfiguration.getOrderDir()).willReturn(ORDER_DIRECTION);

        // when
        nonCategorizedEntryPageDataAdapter.retrieve(ENTRY_PAGE_KEY);

        // then
        verify(entryBridgeService).getPageOfPublicEntries(PAGE_NUMBER, LIMIT, ORDER_BY, ORDER_DIRECTION);
    }

    @Test
    public void shouldStore() {

        // given
        given(entryPageStorageTransformer.transform(ENTRY_PAGE_KEY, RESULT)).willReturn(ENTRY_PAGE);

        // when
        nonCategorizedEntryPageDataAdapter.store(ENTRY_PAGE_KEY, RESULT);

        // then
        verify(entryPageDAO).storePage(ENTRY_PAGE);
    }
}
