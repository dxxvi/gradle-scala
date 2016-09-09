package home.map;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by ly on 8/28/16.
 */
@org.springframework.context.annotation.Configuration
@EnableTransactionManagement
public class Configuration {
    @Bean DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:tcp://localhost/./usa-maps/usa-maps");
        hikariConfig.setUsername("sa");
        hikariConfig.setPassword("");
        return new HikariDataSource(hikariConfig);
    }

    @Bean PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean DataAccessService dataAccessService(NamedParameterJdbcTemplate jdbcTemplate) {
        return new DataAccessService(jdbcTemplate);
    }
}
