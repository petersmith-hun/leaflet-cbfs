package hu.psprog.leaflet.cbfs.persistence.config;

import hu.psprog.leaflet.cbfs.config.DataSourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * Configuration for CBFS persistence integration tests.
 *
 * @author Peter Smith
 */
@Profile(FailoverPersistenceITConfig.PERSISTENCE_IT)
@Configuration
@Import(DataSourceConfig.class)
public class FailoverPersistenceITConfig {

    public static final String PERSISTENCE_IT = "persistence-it";

    public static final String INTEGRATION_TEST_DB_SCRIPT_CATEGORIES = "classpath:persistence_it_db_script_categories.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_DOCUMENTS = "classpath:persistence_it_db_script_documents.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_ENTRIES = "classpath:persistence_it_db_script_entries.sql";
    public static final String INTEGRATION_TEST_DB_SCRIPT_PAGES = "classpath:persistence_it_db_script_pages.sql";
}
