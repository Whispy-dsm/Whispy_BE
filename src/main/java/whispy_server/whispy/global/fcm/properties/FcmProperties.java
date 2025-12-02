package whispy_server.whispy.global.fcm.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Firebase Admin 인증 키 정보를 외부 설정에서 바인딩한 레코드.
 */
@ConfigurationProperties(prefix = "fcm.key")
public record FcmProperties(
        String type,
        String projectId,
        String privateKeyId,
        String privateKey,
        String clientEmail,
        String clientId,
        String authUri,
        String tokenUri,
        String authProviderX509CertUrl,
        String clientX509CertUrl,
        String universeDomain
) {}

