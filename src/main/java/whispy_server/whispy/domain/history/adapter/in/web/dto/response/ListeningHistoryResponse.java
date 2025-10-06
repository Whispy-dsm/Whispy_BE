package whispy_server.whispy.domain.history.adapter.in.web.dto.response;

import whispy_server.whispy.domain.history.adapter.out.dto.ListeningHistoryWithMusicDto;
import whispy_server.whispy.domain.music.model.type.MusicCategory;
import java.time.LocalDateTime;
import java.util.List;

public record ListeningHistoryResponse(
        Long musicId,
        String title,
        String filePath,
        Integer duration,
        MusicCategory category,
        LocalDateTime listenedAt
) {
    public static ListeningHistoryResponse from(ListeningHistoryWithMusicDto dto) {
        return new ListeningHistoryResponse(
                dto.musicId(),
                dto.title(),
                dto.filePath(),
                dto.duration(),
                dto.category(),
                dto.listenedAt()
        );
    }
}
