package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import hu.psprog.leaflet.cbfs.domain.Entry;
import hu.psprog.leaflet.cbfs.persistence.EntryDAO;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EntrySnapshotRetrievalService}.
 * 
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class EntrySnapshotRetrievalServiceTest {

    private static final String LINK = "test-link-1";
    private static final String ENTRY_CONTENT = "{entry-content}";
    
    @Mock
    private EntryDAO entryDAO;
    
    @InjectMocks
    private EntrySnapshotRetrievalService service;


    @Test
    public void shouldRetrieveFilledDataWithExistingEntry() {

        // given
        given(entryDAO.getByLink(LINK)).willReturn(prepareEntry());

        // when
        String result = service.retrieve(LINK);

        // then
        assertThat(result, equalTo(ENTRY_CONTENT));
        verify(entryDAO).getByLink(LINK);
    }

    @Test
    public void shouldRetrieveEmptyResponseDataWithoutExistingEntry() {

        // given
        given(entryDAO.getByLink(LINK)).willReturn(null);

        // when
        String result = service.retrieve(LINK);

        // then
        assertThat(result, equalTo(StringUtils.EMPTY));
        verify(entryDAO).getByLink(LINK);
    }

    private Entry prepareEntry() {
        return Entry.getBuilder()
                .withContent(ENTRY_CONTENT)
                .build();
    }
}
