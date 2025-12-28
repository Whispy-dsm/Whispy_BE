package whispy_server.whispy.domain.notification.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.ToString;
import whispy_server.whispy.domain.topic.model.types.NotificationTopic;

import java.util.List;
import java.util.Map;

/**
 * 알림 전송 요청 DTO.
 *
 * 특정 디바이스 토큰으로 FCM 푸시 알림을 전송하기 위한 요청 정보입니다.
 *
 * @param email 사용자 이메일
 * @param deviceTokens 디바이스 토큰 목록
 * @param topic 알림 주제
 * @param title 알림 제목
 * @param body 알림 내용
 * @param data 추가 데이터
 */
@ToString(exclude = {"deviceTokens"})
@Schema(description = "알림 전송 요청")
public record NotificationSendRequest(
        @Schema(description = "사용자 이메일", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email
        @NotBlank
        @Size(max = 255)
        String email,
        @Schema(description = "디바이스 토큰 목록", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty
        List<String> deviceTokens,
        @Schema(description = "알림 주제", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        NotificationTopic topic,
        @Schema(description = "알림 제목", example = "새로운 알림", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 255)
        String title,
        @Schema(description = "알림 내용", example = "알림 메시지 내용입니다", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 5000)
        String body,
        @Schema(description = "추가 데이터")
        Map<String, String> data
) {}
