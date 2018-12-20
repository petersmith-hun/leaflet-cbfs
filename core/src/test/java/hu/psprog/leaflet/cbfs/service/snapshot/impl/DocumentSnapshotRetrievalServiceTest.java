package hu.psprog.leaflet.cbfs.service.snapshot.impl;

import hu.psprog.leaflet.cbfs.domain.Document;
import hu.psprog.leaflet.cbfs.persistence.DocumentDAO;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DocumentSnapshotRetrievalService}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentSnapshotRetrievalServiceTest {

    private static final String LINK = "test-link-1";
    private static final String DOCUMENT_CONTENT = "{document-content}";

    @Mock
    private DocumentDAO documentDAO;

    @InjectMocks
    private DocumentSnapshotRetrievalService service;

    @Test
    public void shouldRetrieveFilledDataWithExistingDocument() {

        // given
        given(documentDAO.getByLink(LINK)).willReturn(prepareDocument());

        // when
        String result = service.retrieve(LINK);

        // then
        assertThat(result, equalTo(DOCUMENT_CONTENT));
        verify(documentDAO).getByLink(LINK);
    }

    @Test
    public void shouldRetrieveEmptyResponseDataWithoutExistingDocument() {

        // given
        given(documentDAO.getByLink(LINK)).willReturn(null);

        // when
        String result = service.retrieve(LINK);

        // then
        assertThat(result, equalTo(StringUtils.EMPTY));
        verify(documentDAO).getByLink(LINK);
    }

    private Document prepareDocument() {
        return Document.getBuilder()
                .withContent(DOCUMENT_CONTENT)
                .build();
    }
}
