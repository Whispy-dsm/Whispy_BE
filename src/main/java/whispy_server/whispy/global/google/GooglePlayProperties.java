package whispy_server.whispy.global.google;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 구글 플레이 API 연동에 필요한 설정 값을 담는 프로퍼티.
 */
@ConfigurationProperties(prefix = "google.play")
public record GooglePlayProperties(
        String packageName,
        String serviceAccountKeyPath
) {}
