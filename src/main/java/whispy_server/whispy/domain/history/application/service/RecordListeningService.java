package whispy_server.whispy.domain.history.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.history.application.port.in.RecordListeningUseCase;
import whispy_server.whispy.domain.history.application.port.out.QueryListeningHistoryPort;
import whispy_server.whispy.domain.history.application.port.out.SaveListeningHistoryPort;
import whispy_server.whispy.domain.history.model.ListeningHistory;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 청취 기록 저장 UseCase 구현체.
 */
@Service
@RequiredArgsConstructor
public class RecordListeningService implements RecordListeningUseCase {

    private final SaveListeningHistoryPort saveListeningHistoryPort;
    private final QueryListeningHistoryPort queryListeningHistoryPort;
    private final QueryMusicPort queryMusicPort;
    private final UserFacadeUseCase userFacadeUseCase;

    /**
     * 음악 청취 기록을 저장하거나 갱신한다.
     *
     * @param musicId 청취한 음악 ID
     */
    @Transactional
    @Override
    public void execute(Long musicId) {
        if(!queryMusicPort.existsById(musicId)) {
            throw MusicNotFoundException.EXCEPTION;
        }

        User currentUser = userFacadeUseCase.currentUser();
        Long userId = currentUser.id();

        Optional<ListeningHistory> existingHistory =
                queryListeningHistoryPort.findByUserIdAndMusicId(userId, musicId);

        ListeningHistory history = existingHistory
                .map(ListeningHistory::updateListenedAt)
                .orElseGet(() -> new ListeningHistory(
                        null,
                        userId,
                        musicId,
                        LocalDateTime.now()
                ));

        saveListeningHistoryPort.save(history);
    }
}
