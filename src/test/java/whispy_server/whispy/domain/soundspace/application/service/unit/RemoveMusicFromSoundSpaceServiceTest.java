package whispy_server.whispy.domain.soundspace.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.soundspace.application.port.out.DeleteSoundSpaceMusicPort;
import whispy_server.whispy.domain.soundspace.application.service.RemoveMusicFromSoundSpaceService;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * RemoveMusicFromSoundSpaceService의 단위 테스트 클래스
 *
 * 사운드스페이스 음악 제거 서비스를 검증합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RemoveMusicFromSoundSpaceService 테스트")
class RemoveMusicFromSoundSpaceServiceTest {

    @InjectMocks
    private RemoveMusicFromSoundSpaceService service;

    @Mock
    private DeleteSoundSpaceMusicPort deleteSoundSpaceMusicPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Test
    @DisplayName("사운드스페이스에서 음악을 제거할 수 있다")
    void execute() {
        given(userFacadeUseCase.currentUser()).willReturn(new User(1L, "test@test.com", "pw", "name", null, Gender.MALE, Role.USER, null, null, LocalDateTime.now()));

        service.execute(10L);

        verify(deleteSoundSpaceMusicPort).deleteByUserIdAndMusicId(1L, 10L);
    }
}
