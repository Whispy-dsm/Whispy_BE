package whispy_server.whispy.domain.notification.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 읽지 않은 알림 수 응답 DTO.
 *
 * 사용자의 읽지 않은 알림 개수를 클라이언트에 전달하기 위한 응답 데이터입니다.
 *
 * @param count 읽지 않은 알림 개수
 */
@Schema(description = "읽지 않은 알림 수 응답")
public record UnreadCountResponse(
        @Schema(description = "읽지 않은 알림 개수", example = "5")
        int count
) {}
