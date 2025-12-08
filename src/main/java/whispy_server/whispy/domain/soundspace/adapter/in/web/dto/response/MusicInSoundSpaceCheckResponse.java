package whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 특정 음악의 사운드 스페이스 포함 여부를 알려주는 응답 DTO.
 */
@Schema(description = "음악이 사운드 스페이스에 있는지 확인 응답")
public record MusicInSoundSpaceCheckResponse(
        @Schema(description = "사운드 스페이스 포함 여부", example = "true")
        boolean isInSoundSpace
) {
}
