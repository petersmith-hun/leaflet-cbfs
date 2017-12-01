package hu.psprog.leaflet.cbfs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Data source and DAO bean configuration.
 *
 * @author Peter Smith
 */
@Configuration
@ComponentScan(basePackages = "hu.psprog.leaflet.cbfs.persistence")
@ImportResource("classpath:queries.xml")
public class DataSourceConfig {

    private static final String INIT_SCRIPT = "classpath:failover_init.sql";
    private static final String DATABASE_NAME = "failover";

    @Bean
    public NamedParameterJdbcTemplate failoverJdbcTemplate(DataSource failoverDataSource) {
        return new NamedParameterJdbcTemplate(failoverDataSource);
    }

    @Bean
    public DataSource failoverDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName(DATABASE_NAME)
                .addScript(INIT_SCRIPT)
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource failoverDataSource) {

        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(failoverDataSource);

        return txManager;
    }
}
