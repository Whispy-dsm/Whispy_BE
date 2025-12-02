package whispy_server.whispy.domain.notification.batch.dto;

import whispy_server.whispy.domain.topic.model.types.NotificationTopic;
import java.util.Map;

/**
 * 알림 배치 작업 파라미터.
 *
 * 알림 저장 배치 작업에서 사용되는 파라미터 정보입니다.
 *
 * @param email 수신자 이메일
 * @param title 알림 제목
 * @param body 알림 본문
 * @param topic 알림 토픽
 * @param data 추가 데이터
 */
public record NotificationJobParameters(
        String email,
        String title,
        String body,
        NotificationTopic topic,
        Map<String, String> data
) {}
