package whispy_server.whispy.domain.search.music.adapter.in.web.dto.response;

import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

public record MusicSearchResponse(
        Long id,
        String title,
        String filePath,
        Integer duration,
        MusicCategory category
) {

    public static MusicSearchResponse from(Music music) {
        return new MusicSearchResponse(
                music.id(),
                music.title(),
                music.filePath(),
                music.duration(),
                music.category()
        );
    }
}
