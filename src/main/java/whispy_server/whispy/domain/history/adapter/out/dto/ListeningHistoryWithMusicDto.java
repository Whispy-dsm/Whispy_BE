package whispy_server.whispy.domain.history.adapter.out.dto;

import whispy_server.whispy.domain.music.model.type.MusicCategory;
import java.time.LocalDateTime;

/**
 * 청취 이력과 음악 정보를 함께 담는 DTO.
 */
public record ListeningHistoryWithMusicDto(
        Long musicId,
        String title,
        String filePath,
        Integer duration,
        MusicCategory category,
        String bannerImageUrl,
        LocalDateTime listenedAt
) {
}
