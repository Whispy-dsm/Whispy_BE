package whispy_server.whispy.domain.like.model;

import whispy_server.whispy.global.annotation.Aggregate;

@Aggregate
public record MusicLike(
        Long id,
        Long userId,
        Long musicId
) {
}
