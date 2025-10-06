package whispy_server.whispy.domain.history.adapter.out.dto;

import whispy_server.whispy.domain.music.model.type.MusicCategory;
import java.time.LocalDateTime;

public record ListeningHistoryWithMusicDto(
        Long musicId,
        String title,
        String filePath,
        Integer duration,
        MusicCategory category,
        LocalDateTime listenedAt
) {
}
