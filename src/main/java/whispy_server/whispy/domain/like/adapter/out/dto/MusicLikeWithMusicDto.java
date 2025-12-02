package whispy_server.whispy.domain.like.adapter.out.dto;

import whispy_server.whispy.domain.music.model.type.MusicCategory;

/**
 * 좋아요 엔티티와 음악 세부 정보를 조합한 조회용 DTO.
 */
public record MusicLikeWithMusicDto(
        Long musicId,
        String title,
        String filePath,
        Integer duration,
        MusicCategory category
) {
}
