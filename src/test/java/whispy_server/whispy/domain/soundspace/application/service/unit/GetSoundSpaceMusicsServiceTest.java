package whispy_server.whispy.domain.soundspace.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.soundspace.application.port.out.QuerySoundSpaceMusicPort;
import whispy_server.whispy.domain.soundspace.application.service.GetSoundSpaceMusicsService;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * GetSoundSpaceMusicsService의 단위 테스트 클래스
 * <p>
 * 사운드스페이스 음악 목록 조회 서비스를 검증합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetSoundSpaceMusicsService 테스트")
class GetSoundSpaceMusicsServiceTest {

    @InjectMocks
    private GetSoundSpaceMusicsService service;

    @Mock
    private QuerySoundSpaceMusicPort querySoundSpaceMusicPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Test
    @DisplayName("사운드스페이스 음악 목록을 조회할 수 있다")
    void execute() {
        given(userFacadeUseCase.currentUser()).willReturn(new User(1L, "test@test.com", "pw", "name", null, Gender.MALE, Role.USER, null, null, LocalDateTime.now()));
        given(querySoundSpaceMusicPort.findSoundSpaceMusicsWithDetailByUserId(1L)).willReturn(List.of());

        assertThat(service.execute()).isNotNull();
    }
}
