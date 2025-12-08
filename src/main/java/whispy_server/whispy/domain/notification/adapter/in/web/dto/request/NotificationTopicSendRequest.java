package whispy_server.whispy.domain.notification.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.Map;

/**
 * 토픽별 알림 전송 요청 DTO.
 *
 * FCM 토픽을 통해 특정 주제를 구독한 모든 사용자에게 푸시 알림을 전송하기 위한 요청 정보입니다.
 *
 * @param topic 알림 주제
 * @param title 알림 제목
 * @param body 알림 내용
 * @param data 추가 데이터
 */
@Schema(description = "토픽별 알림 전송 요청")
public record NotificationTopicSendRequest(
        @Schema(description = "알림 주제")
        NotificationTopic topic,
        @Schema(description = "알림 제목", example = "새로운 공지")
        String title,
        @Schema(description = "알림 내용", example = "공지사항 내용입니다")
        String body,
        @Schema(description = "추가 데이터")
        Map<String, String> data
) {
}
