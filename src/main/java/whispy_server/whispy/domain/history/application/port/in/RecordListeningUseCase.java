package whispy_server.whispy.domain.history.application.port.in;

import whispy_server.whispy.global.annotation.UseCase;

/**
 * 사용자의 청취 이력을 기록하는 유스케이스 계약이다.
 */
@UseCase
public interface RecordListeningUseCase {
    /**
     * 특정 음악을 청취했음을 기록한다.
     *
     * @param musicId 청취한 음악 ID
     */
    void execute(Long musicId);
}
