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
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteMusicService implements DeleteMusicUseCase {

    private final QueryMusicPort queryMusicPort;
    private final MusicDeletePort musicDeletePort;
    private final DeleteMusicLikePort deleteMusicLikePort;
    private final DeleteListeningHistoryPort deleteListeningHistoryPort;

    @Transactional
    @Override
    public void execute(Long id) {
        if (!queryMusicPort.existsById(id)) {
            throw MusicNotFoundException.EXCEPTION;
        }

        deleteMusicLikePort.deleteAllByMusicId(id); // (논리적 외래키 처리)
        deleteListeningHistoryPort.deleteAllByMusicId(id); // (논리적 외래키 처리)
        musicDeletePort.deleteById(id);
    }
}