package whispy_server.whispy.global.config.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class FcmConfig {

    @Value("${fcm.service-account-key}")
    private final String serviceAccountKey;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if(FirebaseApp.getApps().isEmpty()) {
            var serviceAccount = new FileInputStream(serviceAccountKey);
            var option = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            return FirebaseApp.initializeApp(option);
        }
        return FirebaseApp.getInstance();
    }
}
