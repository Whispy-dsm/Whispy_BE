package whispy_server.whispy.domain.like.application.port.in;

import whispy_server.whispy.domain.like.adapter.in.web.dto.response.LikedMusicResponse;
import whispy_server.whispy.global.annotation.UseCase;
import java.util.List;

@UseCase
public interface QueryMyLikedMusicsUseCase {
    List<LikedMusicResponse> execute();
}
