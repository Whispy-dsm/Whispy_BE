package whispy_server.whispy.global.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "default.profile")
public record DefaultImageProperties(String defaultImageUrl) {
}
