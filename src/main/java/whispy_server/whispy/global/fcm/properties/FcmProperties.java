package whispy_server.whispy.global.fcm;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fcm.key")
public class FcmProperties {
    private String type;
    private String projectId;
    private String privateKeyId;
    private String privateKey;
    private String clientEmail;
    private String clientId;
    private String authUri;
    private String tokenUri;
    private String authProviderX509CertUrl;
    private String clientX509CertUrl;
}
