package whispy_server.whispy.global.feign.google;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.google.GooglePlayProperties;
import whispy_server.whispy.global.exception.domain.payment.GooglePlayApiException;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class GooglePlayAccessTokenProvider {

    private static final String GOOGLE_PLAY_SCOPE = "https://www.googleapis.com/auth/androidpublisher";
    private final GooglePlayProperties googlePlayProperties;

    public String getAccessToken() {
        try{
            ClassPathResource resource = new ClassPathResource(googlePlayProperties.serviceAccountKeyPath());

            GoogleCredentials credentials = ServiceAccountCredentials
                    .fromStream(resource.getInputStream())
                    .createScoped(Collections.singleton(GOOGLE_PLAY_SCOPE));

            credentials.refresh();
            return credentials.getAccessToken().getTokenValue();

        } catch (IOException e) {
            throw GooglePlayApiException.EXCEPTION;
        }
    }
}
