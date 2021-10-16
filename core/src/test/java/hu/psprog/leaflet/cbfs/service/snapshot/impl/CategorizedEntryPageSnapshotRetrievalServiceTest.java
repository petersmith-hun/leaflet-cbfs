package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.persistence.EntryPageDAO;
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
 * Unit tests for {@link CategorizedEntryPageSnapshotRetrievalService}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CategorizedEntryPageSnapshotRetrievalServiceTest {

    private static final long CATEGORY_ID = 3L;
    private static final int PAGE_NUMBER = 2;
    private static final String CATEGORIZED_PAGE_CONTENT = "{categorized-page-content}";

    @Mock
    private EntryPageDAO entryPageDAO;

    @InjectMocks
    private CategorizedEntryPageSnapshotRetrievalService service;

    @Test
    public void shouldRetrieve() {

        // given
        EntryPageKey entryPageKey = EntryPageKey.build(PAGE_NUMBER, CATEGORY_ID);
        given(entryPageDAO.getPageOfCategory(PAGE_NUMBER, CATEGORY_ID)).willReturn(CATEGORIZED_PAGE_CONTENT);

        // when
        String result = service.retrieve(entryPageKey);

        // then
        assertThat(result, equalTo(CATEGORIZED_PAGE_CONTENT));
        verify(entryPageDAO).getPageOfCategory(PAGE_NUMBER, CATEGORY_ID);
    }
}
