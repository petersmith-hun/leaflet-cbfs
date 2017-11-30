package hu.psprog.leaflet.cbfs.persistence.impl;

import hu.psprog.leaflet.cbfs.domain.Document;
import hu.psprog.leaflet.cbfs.persistence.DocumentDAO;
import hu.psprog.leaflet.cbfs.persistence.config.FailoverPersistenceITConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link DocumentDAOImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FailoverPersistenceITConfig.class)
@ActiveProfiles(FailoverPersistenceITConfig.PERSISTENCE_IT)
public class DocumentDAOImplIT {

    private static final String NEW_DOCUMENT_LINK = "new-document-171130";
    private static final String NEW_DOCUMENT_CONTENT = "{...document json ...}";
    private static final String DOCUMENT_2_LINK = "testdocument-2-171130";

    @Autowired
    private DocumentDAO documentDAO;

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void shouldGetDocumentByLink() {

        // when
        Document result = documentDAO.getByLink(DOCUMENT_2_LINK);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getLink(), equalTo(DOCUMENT_2_LINK));
        assertThat(result.getContent().contains(DOCUMENT_2_LINK), is(true));
    }

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void shouldStoreDocument() {

        // given
        Document documentToStore = prepareDocument();

        // when
        documentDAO.storeDocument(documentToStore);

        // then
        Document storedDocument = documentDAO.getByLink(NEW_DOCUMENT_LINK);
        assertThat(storedDocument, equalTo(documentToStore));
    }

    @Test
    @Transactional
    @Sql(FailoverPersistenceITConfig.INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS)
    public void shouldTruncateDocuments() {

        // when
        documentDAO.truncate();

        // then
        assertThat(documentDAO.getByLink(DOCUMENT_2_LINK), nullValue());
    }

    private Document prepareDocument() {
        return Document.getBuilder()
                .withLink(NEW_DOCUMENT_LINK)
                .withContent(NEW_DOCUMENT_CONTENT)
                .build();
    }
}
