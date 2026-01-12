package whispy_server.whispy.domain.like.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 음악 좋아요 여부 확인 응답 DTO.
 *
 * 사용자가 특정 음악에 좋아요를 눌렀는지 여부를 반환합니다.
 *
 * @param isLiked 좋아요 여부 (true: 좋아요 누름, false: 좋아요 안 누름)
 */
@Schema(description = "음악 좋아요 여부 확인 응답")
public record CheckMusicLikeResponse(
        @Schema(description = "좋아요 여부", example = "true")
        boolean isLiked
) {
}
