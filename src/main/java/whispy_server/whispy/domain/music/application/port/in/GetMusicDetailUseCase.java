package whispy_server.whispy.domain.music.application.port.in;

import whispy_server.whispy.domain.music.adapter.in.web.dto.response.MusicDetailResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface GetMusicDetailUseCase {
    MusicDetailResponse execute(Long id);
}
