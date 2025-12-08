package whispy_server.whispy.domain.like.model;

import whispy_server.whispy.global.annotation.Aggregate;

/**
 * 음악 좋아요 도메인 모델 (애그리게잇).
 *
 * 사용자가 특정 음악에 좋아요를 표시한 정보를 담고 있는 도메인 모델입니다.
 *
 * @param id 좋아요 ID
 * @param userId 사용자 ID
 * @param musicId 음악 ID
 */
@Aggregate
public record MusicLike(
        Long id,
        Long userId,
        Long musicId
) {
}
