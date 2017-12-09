package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.persistence.EntryPageDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link NonCategorizedEntryPageSnapshotRetrievalService}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class NonCategorizedEntryPageSnapshotRetrievalServiceTest {

    private static final int PAGE_NUMBER = 2;
    private static final String NON_CATEGORIZED_PAGE_CONTENT = "{non-categorized-page-content}";

    @Mock
    private EntryPageDAO entryPageDAO;

    @InjectMocks
    private NonCategorizedEntryPageSnapshotRetrievalService service;

    @Test
    public void shouldRetrieve() {

        // given
        EntryPageKey entryPageKey = EntryPageKey.build(PAGE_NUMBER);
        given(entryPageDAO.getPage(PAGE_NUMBER)).willReturn(NON_CATEGORIZED_PAGE_CONTENT);

        // when
        String result = service.retrieve(entryPageKey);

        // then
        assertThat(result, equalTo(NON_CATEGORIZED_PAGE_CONTENT));
        verify(entryPageDAO).getPage(PAGE_NUMBER);
    }
}
