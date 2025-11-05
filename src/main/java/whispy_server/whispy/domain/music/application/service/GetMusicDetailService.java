package whispy_server.whispy.domain.music.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicDetailResponse;
import whispy_server.whispy.domain.music.application.port.in.GetMusicDetailUseCase;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.music.model.Music;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;

@Service
@RequiredArgsConstructor
public class GetMusicDetailService implements GetMusicDetailUseCase {

    private final QueryMusicPort queryMusicPort;

    @Transactional(readOnly = true)
    @Override
    public MusicDetailResponse execute(Long id) {
        Music music = queryMusicPort.findById(id)
                .orElseThrow(() -> MusicNotFoundException.EXCEPTION);

        return MusicDetailResponse.from(music);
    }
}
