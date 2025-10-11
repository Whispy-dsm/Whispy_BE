package whispy_server.whispy.domain.soundspace.model;

import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;

@Aggregate
public record SoundSpaceMusic(
        Long id,
        Long userId,
        Long musicId,
        LocalDateTime addedAt
) {
}
