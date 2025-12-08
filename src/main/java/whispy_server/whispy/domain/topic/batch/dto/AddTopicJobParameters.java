package whispy_server.whispy.domain.topic.batch.dto;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

/**
 * 토픽 추가 배치 작업 파라미터.
 *
 * 배치 작업 처리를 위한 토픽 추가 정보를 담고 있습니다.
 *
 * @param email 사용자 이메일
 * @param fcmToken FCM 토큰
 * @param topic 알림 토픽
 * @param defaultSubscribed 기본 구독 여부
 */
public record AddTopicJobParameters(
        String email,
        String fcmToken,
        NotificationTopic topic,
        boolean defaultSubscribed
) {}
