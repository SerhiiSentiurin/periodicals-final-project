package periodicals.epam.com.project.infrastructure.config;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log4j2
public class ConfigLoader {
    @Getter
    private final Map<String, String> configs;

    public ConfigLoader() {
        this.configs = new HashMap<>();
    }

    public void loadConfig(String configPath) {
        Properties property = new Properties();
        try {
            log.info("start load config");
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configPath);

            property.load(inputStream);
            property.stringPropertyNames().stream()
                    .iterator()
                    .forEachRemaining(
                            name -> configs.put(name, property.getProperty(name))
                    );
            log.info("configs was loaded successfully");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
