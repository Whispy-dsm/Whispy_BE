package whispy_server.whispy.global.utils.fcm;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.notification.application.port.out.FcmSendPort;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.exception.domain.fcm.FcmSendFailedException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Firebase Cloud Messaging 전송을 담당하는 유틸 컴포넌트.
 *
 * test 프로파일에서는 비활성화됩니다.
 */
@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!test")
public class FcmUtil implements FcmSendPort {

    private final FirebaseApp firebaseApp;

    /**
     * FirebaseMessaging 인스턴스를 반환합니다.
     *
     * FirebaseApp으로부터 FirebaseMessaging 싱글톤 인스턴스를 가져옵니다.
     *
     * @return FirebaseMessaging 인스턴스
     */
    private FirebaseMessaging getFirebaseMessaging(){
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    /**
     * 다수의 단말 토큰으로 알림을 전송한다.
     */
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
            throw new FcmSendFailedException(e);
        }
    }

    /**
     * 특정 토픽 구독자에게 알림을 전송한다.
     */
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
            throw new FcmSendFailedException(e);
        }
    }

    /**
     * 단일 디바이스를 토픽에 구독시킨다.
     */
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
            throw new FcmSendFailedException(e);
        }
    }

    /**
     * 단일 디바이스의 토픽 구독을 해제한다.
     */
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
            throw new FcmSendFailedException(e);
        }
    }


    /**
     * 텍스트 타이틀/본문으로 FCM Notification 객체를 생성합니다.
     *
     * 알림 바에 표시될 제목과 본문을 포함한 Notification 객체를 생성합니다.
     *
     * @param title 알림 제목
     * @param body  알림 본문
     * @return FCM Notification 객체
     */
    private Notification buildNotification(String title, String body){
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

    /**
     * 안드로이드 디바이스에서 받을 커스텀 데이터 구성을 생성합니다.
     *
     * 알림과 함께 전달될 커스텀 데이터를 Android 전용 Config로 생성합니다.
     * 앱이 포그라운드 상태일 때 알림 커스터마이징에 사용됩니다.
     *
     * @param title 알림 제목
     * @param body  알림 본문
     * @param topic 알림 토픽 (null 가능)
     * @return AndroidConfig 객체
     */
    private AndroidConfig buildAndroidConfig(String title, String body, NotificationTopic topic){
        return AndroidConfig.builder()
                .putData("topic", topic != null ? topic.name() : "")
                .putData("title", title)
                .putData("body", body)
                .build();
    }
}
