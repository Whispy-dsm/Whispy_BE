package whispy_server.whispy.global.google;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.play")
public record GooglePlayProperties(
        String packageName,
        String serviceAccountKeyPath
) {}
