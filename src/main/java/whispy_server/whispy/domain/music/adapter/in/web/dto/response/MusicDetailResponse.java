package whispy_server.whispy.domain.music.adapter.in.web.dto.response;

import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.domain.music.model.type.MusicCategory;

public record MusicDetailResponse(
        String title,
        String filePath,
        Integer duration,
        MusicCategory category,
        String bannerImageUrl
) {

    public static MusicDetailResponse from(Music music) {
        return new MusicDetailResponse(
                music.title(),
                music.filePath(),
                music.duration(),
                music.category(),
                music.bannerImageUrl()
        );
    }
}