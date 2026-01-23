package whispy_server.whispy.global.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.List;
import java.util.Map;

/**
 * 테스트 환경에서 사용할 FCM Mock 설정.
 *
 * 통합 테스트에서 실제 Firebase 연결 없이 테스트할 수 있도록
 * FcmSendPort를 Mock 구현체로 제공합니다.
 */
@TestConfiguration
public class TestFcmConfig {

    @Bean
    @Primary
    public FcmSendPort fcmSendPort() {
        return new FcmSendPort() {
            @Override
            public void sendMulticast(List<String> deviceTokens, String title, String body, Map<String, String> data) {
                // 테스트 환경에서는 실제로 FCM을 전송하지 않음
            }

            @Override
            public void sendToTopic(NotificationTopic topic, String title, String body, Map<String, String> data) {
                // 테스트 환경에서는 실제로 FCM을 전송하지 않음
            }

            @Override
            public void subscribeToTopic(String deviceToken, NotificationTopic topic) {
                // 테스트 환경에서는 실제로 토픽 구독하지 않음
            }

            @Override
            public void unsubscribeFromTopic(String deviceToken, NotificationTopic topic) {
                // 테스트 환경에서는 실제로 토픽 구독 해제하지 않음
            }
        };
    }
}
