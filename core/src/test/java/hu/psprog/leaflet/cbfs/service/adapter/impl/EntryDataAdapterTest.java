package hu.psprog.leaflet.cbfs.service.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.cbfs.domain.Entry;
import hu.psprog.leaflet.cbfs.persistence.EntryDAO;
import hu.psprog.leaflet.cbfs.service.impl.CreationDateLimitValidator;
import hu.psprog.leaflet.cbfs.service.transformer.impl.EntryStorageTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EntryDataAdapter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryDataAdapterTest {

    private static final String LINK = "link-1";
    private static final WrapperBodyDataModel<ExtendedEntryDataModel> RESULT = WrapperBodyDataModel.getBuilder()
            .withBody(ExtendedEntryDataModel.getExtendedBuilder()
                    .withLink(LINK)
                    .build())
            .build();
    private static final Entry ENTRY = Entry.getBuilder().build();

    @Mock
    private EntryBridgeService entryBridgeService;

    @Mock
    private EntryStorageTransformer entryStorageTransformer;

    @Mock
    private EntryDAO entryDAO;

    @Mock
    private CreationDateLimitValidator creationDateLimitValidator;

    @InjectMocks
    private EntryDataAdapter entryDataAdapter;

    @Before
    public void setup() {
        given(entryStorageTransformer.transform(LINK, RESULT)).willReturn(ENTRY);
    }

    @Test
    public void shouldRetrieve() throws CommunicationFailureException {

        // when
        entryDataAdapter.retrieve(LINK);

        // then
        verify(entryBridgeService).getEntryByLink(LINK);
    }

    @Test
    public void shouldStoreIfCreationDateIsValidated() {

        // given
        given(creationDateLimitValidator.isValid(RESULT)).willReturn(true);

        // when
        entryDataAdapter.store(LINK, RESULT);

        // then
        verify(entryDAO).storeEntry(ENTRY);
    }

    @Test
    public void shouldNotStoreIfCreationDateIsInvalid() {

        // given
        given(creationDateLimitValidator.isValid(RESULT)).willReturn(false);

        // when
        entryDataAdapter.store(LINK, RESULT);

        // then
        verify(entryDAO, never()).storeEntry(ENTRY);
    }
}
