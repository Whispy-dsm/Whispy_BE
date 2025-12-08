package whispy_server.whispy.domain.soundspace.adapter.out.dto;

import whispy_server.whispy.domain.music.model.type.MusicCategory;

/**
 * 사운드 스페이스 엔트리와 음악 세부 정보를 함께 담는 DTO.
 */
public record SoundSpaceMusicWithDetailDto(
        Long musicId,
        String title,
        String filePath,
        Integer duration,
        MusicCategory category
) {
}
