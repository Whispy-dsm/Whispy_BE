package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import whispy_server.whispy.domain.music.application.port.in.DeleteMusicUseCase;
import whispy_server.whispy.domain.music.application.port.out.MusicPort;
import whispy_server.whispy.domain.search.music.application.port.out.DeleteIndexPort;
import whispy_server.whispy.domain.search.music.application.port.out.SearchMusicPort;
import whispy_server.whispy.global.annotation.UseCase;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteMusicService implements DeleteMusicUseCase {

    private final MusicPort musicPort;
    private final DeleteIndexPort deleteIndexPort;

    @Transactional
    @Override
    public void execute(Long id) {
        if (!musicPort.existsById(id)) {
            throw MusicNotFoundException.EXCEPTION;
        }

        musicPort.deleteById(id);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    deleteIndexPort.deleteFromIndex(id);
                } catch (Exception e) {
                    log.warn("Failed to delete music from index", e);
                }
            }
        });
    }
}