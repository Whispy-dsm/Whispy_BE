package whispy_server.whispy.domain.like.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.like.adapter.in.web.dto.response.CheckMusicLikeResponse;
import whispy_server.whispy.domain.like.application.port.out.QueryMusicLikePort;
import whispy_server.whispy.domain.like.application.service.CheckMusicLikeService;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * CheckMusicLikeService의 단위 테스트 클래스
 *
 * 음악 좋아요 확인 서비스의 다양한 시나리오를 검증합니다.
 * 좋아요 여부 조회 로직을 테스트합니다.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CheckMusicLikeService 테스트")
class CheckMusicLikeServiceTest {

    @InjectMocks
    private CheckMusicLikeService checkMusicLikeService;

    @Mock
    private QueryMusicLikePort queryMusicLikePort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_MUSIC_ID = 10L;
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("좋아요한 음악에 대해 true를 반환한다")
    void whenMusicIsLiked_thenReturnsTrue() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMusicLikePort.existsByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID))
                .willReturn(true);

        // when
        CheckMusicLikeResponse result = checkMusicLikeService.execute(TEST_MUSIC_ID);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isLiked()).isTrue();
        verify(userFacadeUseCase).currentUser();
        verify(queryMusicLikePort).existsByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID);
    }

    @Test
    @DisplayName("좋아요하지 않은 음악에 대해 false를 반환한다")
    void whenMusicIsNotLiked_thenReturnsFalse() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMusicLikePort.existsByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID))
                .willReturn(false);

        // when
        CheckMusicLikeResponse result = checkMusicLikeService.execute(TEST_MUSIC_ID);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isLiked()).isFalse();
        verify(userFacadeUseCase).currentUser();
        verify(queryMusicLikePort).existsByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID);
    }

    @Test
    @DisplayName("다른 음악에 대해서도 좋아요 여부를 확인할 수 있다")
    void whenDifferentMusic_thenChecksCorrectly() {
        // given
        Long differentMusicId = 20L;
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMusicLikePort.existsByUserIdAndMusicId(TEST_USER_ID, differentMusicId))
                .willReturn(true);

        // when
        CheckMusicLikeResponse result = checkMusicLikeService.execute(differentMusicId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isLiked()).isTrue();
        verify(queryMusicLikePort).existsByUserIdAndMusicId(TEST_USER_ID, differentMusicId);
    }

    /**
     * 테스트용 User 객체를 생성합니다.
     *
     * @return 생성된 User 객체
     */
    private User createUser() {
        return new User(
                TEST_USER_ID,
                TEST_EMAIL,
                "password",
                "TestUser",
                null,
                Gender.MALE,
                Role.USER,
                null,
                null,
                LocalDateTime.now()
        );
    }
}
