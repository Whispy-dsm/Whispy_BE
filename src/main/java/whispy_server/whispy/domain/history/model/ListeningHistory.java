package whispy_server.whispy.domain.history.model;

import whispy_server.whispy.global.annotation.Aggregate;
import java.time.LocalDateTime;

/**
 * 청취 히스토리 도메인 모델 (애그리게잇).
 *
 * 사용자의 음악 청취 기록을 담고 있는 도메인 모델입니다.
 *
 * @param id 히스토리 ID
 * @param userId 사용자 ID
 * @param musicId 음악 ID
 * @param listenedAt 청취 일시
 */
@Aggregate
public record ListeningHistory(
        Long id,
        Long userId,
        Long musicId,
        LocalDateTime listenedAt
) {
    /**
     * 청취 일시를 현재 시간으로 업데이트합니다.
     *
     * @return 업데이트된 청취 히스토리
     */
    public ListeningHistory updateListenedAt() {
        return new ListeningHistory(
                this.id,
                this.userId,
                this.musicId,
                LocalDateTime.now()
        );
    }
}
