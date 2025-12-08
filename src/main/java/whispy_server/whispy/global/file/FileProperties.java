package whispy_server.whispy.global.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 파일 업로드 경로와 접근 URL 등을 보유한 구성 프로퍼티.
 */
@ConfigurationProperties("spring.whispy.file")
public record FileProperties(
        String uploadPath,
        String baseUrl
) {
}
