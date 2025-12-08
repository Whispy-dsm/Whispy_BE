package whispy_server.whispy.domain.notification.model;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 알림 도메인 모델 (애그리게잇).
 *
 * FCM 푸시 알림 정보를 담고 있는 도메인 모델입니다.
 *
 * @param id 알림 ID
 * @param email 수신자 이메일
 * @param title 알림 제목
 * @param body 알림 본문
 * @param topic 알림 토픽
 * @param data 추가 데이터
 * @param read 읽음 여부
 * @param createdAt 생성일시
 */
@Aggregate
public record Notification(
        Long id,
        String email,
        String title,
        String body,
        NotificationTopic topic,
        Map<String, String> data,
        boolean read,
        LocalDateTime createdAt
) {

    /**
     * 알림을 읽음 상태로 변경합니다.
     *
     * @return 읽음 상태로 변경된 알림
     */
    public Notification markAsRead() {
        return new Notification(
                this.id,
                this.email,
                this.title,
                this.body,
                this.topic,
                this.data,
                true,
                this.createdAt
        );
    }
}
