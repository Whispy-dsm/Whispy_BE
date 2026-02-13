package whispy_server.whispy.domain.history.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import whispy_server.whispy.domain.history.adapter.out.dto.ListeningHistoryWithMusicDto;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import java.time.LocalDateTime;

/**
 * 청취 이력 조회 응답 DTO.
 */
@Schema(description = "청취 기록 응답")
public record ListeningHistoryResponse(
        @Schema(description = "음악 ID", example = "1")
        Long musicId,
        @Schema(description = "음악 제목", example = "편안한 빗소리")
        String title,
        @Schema(description = "파일 경로", example = "/music/rain.mp3")
        String filePath,
        @Schema(description = "음악 길이(초)", example = "180")
        Integer duration,
        @Schema(description = "음악 카테고리")
        MusicCategory category,
        @Schema(description = "배너 이미지 URL", example = "https://example.com/image.jpg")
        String bannerImageUrl,
        @Schema(description = "청취 일시", example = "2024-01-01T12:00:00")
        LocalDateTime listenedAt
) {
    /**
     * 도메인 DTO를 응답으로 변환한다.
     *
     * @param dto 청취 이력 DTO
     * @return 응답 객체
     */
    public static ListeningHistoryResponse from(ListeningHistoryWithMusicDto dto) {
        return new ListeningHistoryResponse(
                dto.musicId(),
                dto.title(),
                dto.filePath(),
                dto.duration(),
                dto.category(),
                dto.bannerImageUrl(),
                dto.listenedAt()
        );
    }
}
