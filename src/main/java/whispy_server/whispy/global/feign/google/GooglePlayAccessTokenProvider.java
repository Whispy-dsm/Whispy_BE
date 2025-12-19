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

/**
 * 구글 플레이 퍼블리셔 API 호출에 필요한 액세스 토큰을 발급하는 컴포넌트.
 */
@Component
@RequiredArgsConstructor
public class GooglePlayAccessTokenProvider {

    private static final String GOOGLE_PLAY_SCOPE = "https://www.googleapis.com/auth/androidpublisher";
    private final GooglePlayProperties googlePlayProperties;

    /**
     * 서비스 계정 키로 액세스 토큰을 발급받아 반환한다.
     */
    public String getAccessToken() {
        try{
            ClassPathResource resource = new ClassPathResource(googlePlayProperties.serviceAccountKeyPath());

            GoogleCredentials credentials = ServiceAccountCredentials
                    .fromStream(resource.getInputStream())
                    .createScoped(Collections.singleton(GOOGLE_PLAY_SCOPE));

            credentials.refresh();
            return credentials.getAccessToken().getTokenValue();

        } catch (IOException e) {
            throw new GooglePlayApiException(e);
        }
    }
}
