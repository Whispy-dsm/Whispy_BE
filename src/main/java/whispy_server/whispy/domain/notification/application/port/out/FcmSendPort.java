package whispy_server.whispy.domain.notification.application.port.out;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.List;
import java.util.Map;

/**
 * FCM 전송 아웃바운드 포트.
 *
 * Firebase Cloud Messaging을 통한 푸시 알림 전송 및 토픽 구독 관리를 정의하는 아웃바운드 포트입니다.
 */
public interface FcmSendPort {
    /**
     * 여러 디바이스 토큰으로 알림을 전송합니다.
     *
     * @param tokens 디바이스 토큰 목록
     * @param title 알림 제목
     * @param body 알림 본문
     * @param data 추가 데이터
     */
    void sendMulticast(List<String> tokens, String title, String body, Map<String, String> data);

    /**
     * 특정 토픽으로 알림을 전송합니다.
     *
     * @param topic 알림 토픽
     * @param title 알림 제목
     * @param body 알림 본문
     * @param data 추가 데이터
     */
    void sendToTopic(NotificationTopic topic, String title, String body, Map<String, String> data);

    /**
     * 디바이스 토큰을 특정 토픽에 구독합니다.
     *
     * @param deviceToken 디바이스 토큰
     * @param topic 구독할 토픽
     */
    void subscribeToTopic(String deviceToken, NotificationTopic topic);

    /**
     * 디바이스 토큰을 특정 토픽에서 구독 해제합니다.
     *
     * @param deviceToken 디바이스 토큰
     * @param topic 구독 해제할 토픽
     */
    void unsubscribeFromTopic(String deviceToken, NotificationTopic topic);
}
