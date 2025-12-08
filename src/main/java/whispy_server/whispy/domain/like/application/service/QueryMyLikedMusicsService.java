package whispy_server.whispy.domain.like.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.like.adapter.in.web.dto.response.LikedMusicResponse;
import whispy_server.whispy.domain.like.adapter.out.dto.MusicLikeWithMusicDto;
import whispy_server.whispy.domain.like.application.port.in.QueryMyLikedMusicsUseCase;
import whispy_server.whispy.domain.like.application.port.out.QueryMusicLikePort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

import java.util.List;

/**
 * 사용자 ID 로 좋아요한 음악 목록을 조회해 응답 DTO 로 변환하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class QueryMyLikedMusicsService implements QueryMyLikedMusicsUseCase {

    private final QueryMusicLikePort queryMusicLikePort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Transactional(readOnly = true)
    @Override
    public List<LikedMusicResponse> execute() {
        User currentUser = userFacadeUseCase.currentUser();
        List<MusicLikeWithMusicDto> dtos = queryMusicLikePort.findLikedMusicsWithDetailByUserId(currentUser.id());

        return LikedMusicResponse.fromList(dtos);
    }
}
