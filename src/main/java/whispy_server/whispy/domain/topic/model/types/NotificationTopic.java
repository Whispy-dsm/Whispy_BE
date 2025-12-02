package whispy_server.whispy.domain.topic.model.types;

/**
 * 알림 토픽 유형.
 *
 * FCM 토픽 구독에 사용되는 알림 주제를 정의합니다.
 */
public enum NotificationTopic {
    /** 일반 공지사항 */
    GENERAL_ANNOUNCEMENT,
    /** 시스템 공지사항 */
    SYSTEM_ANNOUNCEMENT,
    /** 브로드캐스트 공지사항 */
    BROADCAST_ANNOUNCEMENT,
    /** 관리자 전용 */
    ONLY_ADMIN
}
