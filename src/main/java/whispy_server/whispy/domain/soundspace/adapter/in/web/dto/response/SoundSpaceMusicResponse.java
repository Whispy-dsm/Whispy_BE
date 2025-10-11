package whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response;

import whispy_server.whispy.domain.music.model.type.MusicCategory;
import whispy_server.whispy.domain.soundspace.adapter.out.dto.SoundSpaceMusicWithDetailDto;

import java.util.List;

public record SoundSpaceMusicResponse(
        Long musicId,
        String title,
        String filePath,
        Integer duration,
        MusicCategory category
) {
    public static List<SoundSpaceMusicResponse> fromList(List<SoundSpaceMusicWithDetailDto> dtos) {
        return dtos.stream()
                .map(dto -> new SoundSpaceMusicResponse(
                        dto.musicId(),
                        dto.title(),
                        dto.filePath(),
                        dto.duration(),
                        dto.category()
                ))
                .toList();
    }
}
