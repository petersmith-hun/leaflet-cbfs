package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.cbfs.domain.MirrorType;
import hu.psprog.leaflet.cbfs.persistence.EntryPageDAO;
import hu.psprog.leaflet.cbfs.service.FailoverStatusService;
import hu.psprog.leaflet.cbfs.service.adapter.impl.EntryDataAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EntryDataMirroringService}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryDataMirroringServiceTest {

    private static final WrapperBodyDataModel<ExtendedEntryDataModel> ENTRY_DATA = WrapperBodyDataModel.getBuilder().build();
    private static final String LINK_1 = "link-1";
    private static final String LINK_2 = "link-2";
    private static final String LINK_3 = "link-3";

    @Mock
    private EntryDataAdapter entryDataAdapter;

    @Mock
    private EntryPageDAO entryPageDAO;

    @Mock
    private FailoverStatusService failoverStatusService;

    @InjectMocks
    private EntryDataMirroringService entryDataMirroringService;

    @Before
    public void setup() throws CommunicationFailureException {
        given(entryPageDAO.collectAllEntryLinks()).willReturn(prepareLinkSet());
        given(entryDataAdapter.retrieve(anyString())).willReturn(ENTRY_DATA);
    }

    @Test
    public void shouldLoadEntries() throws CommunicationFailureException {

        // when
        entryDataMirroringService.load();

        // then
        verify(entryDataAdapter).retrieve(LINK_1);
        verify(entryDataAdapter).retrieve(LINK_2);
        verify(entryDataAdapter).retrieve(LINK_3);
        verify(entryDataAdapter).store(LINK_1, ENTRY_DATA);
        verify(entryDataAdapter).store(LINK_2, ENTRY_DATA);
        verify(entryDataAdapter).store(LINK_3, ENTRY_DATA);
    }

    @Test
    public void shouldFailSilentlyOnError() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(entryDataAdapter).retrieve(anyString());

        // when
        entryDataMirroringService.load();

        // then
        // silent fail
        verify(failoverStatusService, times(3)).markMirroringFailure(MirrorType.ENTRY);
    }

    private Set<String> prepareLinkSet() {

        Set<String> linkSet = new HashSet<>();
        linkSet.add(LINK_1);
        linkSet.add(LINK_2);
        linkSet.add(LINK_3);

        return linkSet;
    }
}
