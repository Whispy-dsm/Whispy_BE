package whispy_server.whispy.domain.soundspace.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.soundspace.application.port.out.DeleteSoundSpaceMusicPort;
import whispy_server.whispy.domain.soundspace.application.port.out.QuerySoundSpaceMusicPort;
import whispy_server.whispy.domain.soundspace.application.port.out.SaveSoundSpaceMusicPort;
import whispy_server.whispy.domain.soundspace.application.service.ToggleSoundSpaceMusicService;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * ToggleSoundSpaceMusicService의 단위 테스트 클래스
 * <p>
 * 사운드스페이스 음악 토글 서비스를 검증합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ToggleSoundSpaceMusicService 테스트")
class ToggleSoundSpaceMusicServiceTest {

    @InjectMocks
    private ToggleSoundSpaceMusicService service;

    @Mock
    private SaveSoundSpaceMusicPort saveSoundSpaceMusicPort;

    @Mock
    private QuerySoundSpaceMusicPort querySoundSpaceMusicPort;

    @Mock
    private DeleteSoundSpaceMusicPort deleteSoundSpaceMusicPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Mock
    private QueryMusicPort queryMusicPort;

    @Test
    @DisplayName("사운드스페이스에 음악을 추가할 수 있다")
    void execute() {
        given(userFacadeUseCase.currentUser()).willReturn(new User(1L, "test@test.com", "pw", "name", null, Gender.MALE, Role.USER, null, null, LocalDateTime.now()));
        given(queryMusicPort.existsById(10L)).willReturn(true);
        given(querySoundSpaceMusicPort.existsByUserIdAndMusicId(1L, 10L)).willReturn(false);

        service.execute(10L);

        verify(saveSoundSpaceMusicPort).save(any());
    }
}
