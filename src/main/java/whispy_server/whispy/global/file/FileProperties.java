package whispy_server.whispy.global.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 파일 접근 URL 설정을 보유한 구성 프로퍼티.
 */
@ConfigurationProperties("spring.whispy.file")
public record FileProperties(
        String baseUrl
) {
}
