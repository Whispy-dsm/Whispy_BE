package whispy_server.whispy.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ConfigurationProperties 로 선언된 클래스들을 스캔하는 설정.
 */
@ConfigurationPropertiesScan("whispy_server.whispy")
@Configuration
public class ConfigurationPropertiesConfig {
}
