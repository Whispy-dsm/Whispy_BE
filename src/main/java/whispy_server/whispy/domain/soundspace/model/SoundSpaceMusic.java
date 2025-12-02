package whispy_server.whispy.domain.soundspace.model;

import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;

/**
 * 사운드스페이스 음악 도메인 모델 (애그리게잇).
 *
 * 사용자가 커스텀 사운드스페이스에 추가한 음악 정보를 담고 있는 도메인 모델입니다.
 *
 * @param id 사운드스페이스 음악 ID
 * @param userId 사용자 ID
 * @param musicId 음악 ID
 * @param addedAt 추가 일시
 */
@Aggregate
public record SoundSpaceMusic(
        Long id,
        Long userId,
        Long musicId,
        LocalDateTime addedAt
) {
}
