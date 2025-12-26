package whispy_server.whispy.domain.soundspace.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.soundspace.adapter.in.web.dto.response.MusicInSoundSpaceCheckResponse;
import whispy_server.whispy.domain.soundspace.application.port.out.QuerySoundSpaceMusicPort;
import whispy_server.whispy.domain.soundspace.application.service.CheckMusicInSoundSpaceService;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * CheckMusicInSoundSpaceService의 단위 테스트 클래스
 *
 * 사운드스페이스 음악 포함 여부 확인 서비스를 검증합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CheckMusicInSoundSpaceService 테스트")
class CheckMusicInSoundSpaceServiceTest {

    @InjectMocks
    private CheckMusicInSoundSpaceService service;

    @Mock
    private QuerySoundSpaceMusicPort querySoundSpaceMusicPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Test
    @DisplayName("음악이 사운드스페이스에 포함되어 있는지 확인할 수 있다")
    void execute() {
        given(userFacadeUseCase.currentUser()).willReturn(new User(1L, "test@test.com", "pw", "name", null, Gender.MALE, Role.USER, null, null, LocalDateTime.now()));
        given(querySoundSpaceMusicPort.existsByUserIdAndMusicId(1L, 10L)).willReturn(true);

        MusicInSoundSpaceCheckResponse response = service.execute(10L);

        assertThat(response.isInSoundSpace()).isTrue();
    }
}
