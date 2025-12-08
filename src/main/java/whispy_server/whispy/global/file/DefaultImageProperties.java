package whispy_server.whispy.global.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 프로필 기본 이미지 관련 설정 값을 바인딩하는 프로퍼티.
 */
@ConfigurationProperties(prefix = "default.profile")
public record DefaultImageProperties(String defaultImageUrl) {
}
