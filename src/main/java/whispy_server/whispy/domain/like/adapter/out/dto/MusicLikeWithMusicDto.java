package whispy_server.whispy.domain.like.adapter.out.dto;

import whispy_server.whispy.domain.music.model.type.MusicCategory;

public record MusicLikeWithMusicDto(
        Long musicId,
        String title,
        String filePath,
        Integer duration,
        MusicCategory category
) {
}
