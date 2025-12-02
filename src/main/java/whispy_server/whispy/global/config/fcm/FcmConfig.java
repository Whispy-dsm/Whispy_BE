package whispy_server.whispy.global.config.fcm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import whispy_server.whispy.global.fcm.properties.FcmProperties;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Firebase Admin SDK 초기화를 위한 설정.
 */
@Configuration
@RequiredArgsConstructor
public class FcmConfig {

    private final ObjectMapper objectMapper;
    private final FcmProperties fcmProperties;

    /**
     * 환경설정 정보를 기반으로 FirebaseApp 인스턴스를 초기화한다.
     */
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {

            String privateKey = fcmProperties.privateKey().replace("\\n", "\n");

            var modifiedProperties = new FcmProperties(
                    fcmProperties.type(),
                    fcmProperties.projectId(),
                    fcmProperties.privateKeyId(),
                    privateKey,
                    fcmProperties.clientEmail(),
                    fcmProperties.clientId(),
                    fcmProperties.authUri(),
                    fcmProperties.tokenUri(),
                    fcmProperties.authProviderX509CertUrl(),
                    fcmProperties.clientX509CertUrl(),
                    fcmProperties.universeDomain()
            );

            String jsonCredentials = objectMapper.writeValueAsString(modifiedProperties);
            InputStream inputStream = new ByteArrayInputStream(
                    jsonCredentials.getBytes(StandardCharsets.UTF_8)
            );

            var option = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            return FirebaseApp.initializeApp(option);
        }
        return FirebaseApp.getInstance();
    }
}
