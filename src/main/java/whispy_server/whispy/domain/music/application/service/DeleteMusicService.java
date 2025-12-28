package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.history.application.port.out.DeleteListeningHistoryPort;
import whispy_server.whispy.domain.like.application.port.out.DeleteMusicLikePort;
import whispy_server.whispy.domain.music.application.port.in.DeleteMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicDeletePort;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.soundspace.application.port.out.DeleteSoundSpaceMusicPort;
import whispy_server.whispy.global.annotation.UserAction;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

/**
 * 음악 삭제 서비스.
 *
 * 음악과 연관된 모든 데이터를 함께 삭제하는 유스케이스 구현체입니다.
 * 좋아요, 청취 히스토리, 사운드 스페이스 데이터를 먼저 삭제한 후 음악을 삭제합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteMusicService implements DeleteMusicUseCase {

    private final QueryMusicPort queryMusicPort;
    private final MusicDeletePort musicDeletePort;
    private final DeleteMusicLikePort deleteMusicLikePort;
    private final DeleteListeningHistoryPort deleteListeningHistoryPort;
    private final DeleteSoundSpaceMusicPort deleteSoundSpaceMusicPort;

    /**
     * 지정된 ID의 음악을 삭제합니다.
     *
     * 음악 삭제 시 연관된 모든 데이터(좋아요, 청취 히스토리, 사운드 스페이스)를 함께 삭제합니다.
     *
     * @param id 삭제할 음악의 ID
     * @throws MusicNotFoundException 음악을 찾을 수 없을 때 발생
     */
    @UserAction("음악 삭제")
    @Transactional
    @Override
    public void execute(Long id) {
        if (!queryMusicPort.existsById(id)) {
            throw MusicNotFoundException.EXCEPTION;
        }

        deleteMusicLikePort.deleteAllByMusicId(id); // (논리적 외래키 처리)
        deleteListeningHistoryPort.deleteAllByMusicId(id); // (논리적 외래키 처리)
        deleteSoundSpaceMusicPort.deleteAllByMusicId(id); // (논리적 외래키 처리)
        musicDeletePort.deleteById(id);
    }
}