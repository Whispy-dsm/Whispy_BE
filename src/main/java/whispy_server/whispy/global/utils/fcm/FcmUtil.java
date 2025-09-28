package whispy_server.whispy.global.utils.fcm;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class FcmUtil implements FcmSendPort {

    private final FirebaseApp firebaseApp;

    private FirebaseMessaging getFirebaseMessaging(){
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Override
    public void sendMulticast(List<String> deviceTokens, String title, String body, Map<String, String> data){

        if (deviceTokens == null || deviceTokens.isEmpty()) {
                log.warn("FCM Token 목록이 비어 있어 알림을 발송하지 않습니다.");
                return;
            }

        List<String> validTokens = deviceTokens.stream()
                .filter(token -> token != null && !token.trim().isEmpty())
                .toList();

        if(validTokens.isEmpty()){
            log.warn("유효한 FCM Token이 없어 알림을 발송하지 않습니다.");
            return;
        }

        try {
            Notification notification = buildNotification(title, body);

            MulticastMessage multicastMessage = MulticastMessage.builder()
                    .addAllTokens(validTokens)
                    .setNotification(notification)
                    .putAllData(data != null ? data : Collections.emptyMap())
                    .setAndroidConfig(buildAndroidConfig(title, body, null))
                    .build();
            getFirebaseMessaging().sendEachForMulticastAsync(multicastMessage);

        }catch (Exception e){
            log.error("FCM 멀티캐스트 전송 실패", e);
        }
    }

    @Override
    public void sendToTopic(NotificationTopic topic, String title, String body, Map<String, String> data) {
        try {
            Notification notification = buildNotification(title, body);

            Message message = Message.builder()
                    .setTopic(topic.name())
                    .setNotification(notification)
                    .putAllData(data != null ? data : Collections.emptyMap())
                    .setAndroidConfig(buildAndroidConfig(title, body, topic))
                    .build();

            getFirebaseMessaging().sendAsync(message);
        } catch (Exception e) {
            log.error("FCM 토픽 구독 실패: topic={}", topic, e);
        }
    }

    @Override
    public void subscribeToTopic(String deviceToken, NotificationTopic topic) {
        if(deviceToken == null || deviceToken.trim().isEmpty()){
            log.warn("FCM Token이 없어 토픽 구독을 처리할 수 없습니다.");
            return;
        }

        try {
            getFirebaseMessaging().subscribeToTopicAsync(
                    Collections.singletonList(deviceToken),
                    topic.name()
            );
        } catch (Exception e){
            log.error("FCM 토픽 구독 실패: topic={}", topic, e);
        }
    }

    @Override
    public void unsubscribeFromTopic(String deviceToken, NotificationTopic topic) {
        if(deviceToken == null || deviceToken.trim().isEmpty()){
            log.warn("FCM Token이 없어 토픽 구독 해제를 처리할 수 없습니다.");
            return;
        }

        try {
            getFirebaseMessaging().unsubscribeFromTopicAsync(
                    Collections.singletonList(deviceToken),
                    topic.name()
            );
        } catch (Exception e){
            log.error("FCM 토픽 구독 해제 실패: topic={}", topic, e);
        }
    }


    private Notification buildNotification(String title, String body){
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

    private AndroidConfig buildAndroidConfig(String title, String body, NotificationTopic topic){
        return AndroidConfig.builder()
                .putData("topic", topic != null ? topic.name() : "")
                .putData("title", title)
                .putData("body", body)
                .build();
    }
}
