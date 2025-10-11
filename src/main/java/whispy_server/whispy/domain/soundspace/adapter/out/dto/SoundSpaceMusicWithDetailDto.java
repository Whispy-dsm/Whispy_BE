package whispy_server.whispy.domain.soundspace.adapter.out.dto;

import whispy_server.whispy.domain.music.model.type.MusicCategory;

public record SoundSpaceMusicWithDetailDto(
        Long musicId,
        String title,
        String filePath,
        Integer duration,
        MusicCategory category
) {
}
