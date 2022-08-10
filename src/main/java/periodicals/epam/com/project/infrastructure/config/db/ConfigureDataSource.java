package periodicals.epam.com.project.infrastructure.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import periodicals.epam.com.project.infrastructure.config.ConfigLoader;

import javax.sql.DataSource;

@Log4j2
public class ConfigureDataSource {
    public DataSource createDataSource(ConfigLoader configLoader) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(configLoader.getConfigs().get("db.url"));
        hikariConfig.setUsername(configLoader.getConfigs().get("db.username"));
        log.info("Set userName - " + configLoader.getConfigs().get("db.username"));
        hikariConfig.setPassword(configLoader.getConfigs().get("db.password"));
        log.info("Set userPassword - " + configLoader.getConfigs().get("db.password"));

        return new HikariDataSource(hikariConfig);
    }
}
