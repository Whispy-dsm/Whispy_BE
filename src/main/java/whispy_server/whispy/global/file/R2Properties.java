package whispy_server.whispy.global.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cloudflare R2 연동 설정 프로퍼티.
 */
@ConfigurationProperties("spring.whispy.r2")
public record R2Properties(
        String accountId,
        String accessKeyId,
        String secretAccessKey,
        String bucket
) {

    /**
     * Cloudflare R2 S3 호환 API 엔드포인트를 생성합니다.
     *
     * @return `https://{accountId}.r2.cloudflarestorage.com` 형식의 엔드포인트
     */
    public String endpoint() {
        return "https://" + accountId + ".r2.cloudflarestorage.com";
    }
}
