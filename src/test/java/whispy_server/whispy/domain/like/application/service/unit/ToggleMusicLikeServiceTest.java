package whispy_server.whispy.domain.like.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.like.application.port.out.DeleteMusicLikePort;
import whispy_server.whispy.domain.like.application.port.out.QueryMusicLikePort;
import whispy_server.whispy.domain.like.application.port.out.SaveMusicLikePort;
import whispy_server.whispy.domain.like.application.service.ToggleMusicLikeService;
import whispy_server.whispy.domain.like.model.MusicLike;
import whispy_server.whispy.domain.music.application.port.out.QueryMusicPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.music.MusicNotFoundException;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * ToggleMusicLikeService의 단위 테스트 클래스
 * <p>
 * 음악 좋아요 토글 서비스의 다양한 시나리오를 검증합니다.
 * 좋아요 추가 및 삭제 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ToggleMusicLikeService 테스트")
class ToggleMusicLikeServiceTest {

    @InjectMocks
    private ToggleMusicLikeService toggleMusicLikeService;

    @Mock
    private SaveMusicLikePort saveMusicLikePort;

    @Mock
    private QueryMusicLikePort queryMusicLikePort;

    @Mock
    private DeleteMusicLikePort deleteMusicLikePort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    @Mock
    private QueryMusicPort queryMusicPort;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_MUSIC_ID = 10L;
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("좋아요하지 않은 음악에 좋아요를 추가할 수 있다")
    void whenNotLiked_thenAddsLike() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMusicPort.existsById(TEST_MUSIC_ID)).willReturn(true);
        given(queryMusicLikePort.existsByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID))
                .willReturn(false);

        // when
        toggleMusicLikeService.execute(TEST_MUSIC_ID);

        // then
        verify(queryMusicPort).existsById(TEST_MUSIC_ID);
        verify(queryMusicLikePort).existsByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID);
        verify(saveMusicLikePort).save(any(MusicLike.class));
        verify(deleteMusicLikePort, never()).deleteByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID);
    }

    @Test
    @DisplayName("이미 좋아요한 음악의 좋아요를 취소할 수 있다")
    void whenAlreadyLiked_thenRemovesLike() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMusicPort.existsById(TEST_MUSIC_ID)).willReturn(true);
        given(queryMusicLikePort.existsByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID))
                .willReturn(true);

        // when
        toggleMusicLikeService.execute(TEST_MUSIC_ID);

        // then
        verify(queryMusicPort).existsById(TEST_MUSIC_ID);
        verify(queryMusicLikePort).existsByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID);
        verify(deleteMusicLikePort).deleteByUserIdAndMusicId(TEST_USER_ID, TEST_MUSIC_ID);
        verify(saveMusicLikePort, never()).save(any(MusicLike.class));
    }

    @Test
    @DisplayName("존재하지 않는 음악에 대해 예외가 발생한다")
    void whenMusicNotFound_thenThrowsException() {
        // given
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMusicPort.existsById(TEST_MUSIC_ID)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> toggleMusicLikeService.execute(TEST_MUSIC_ID))
                .isInstanceOf(MusicNotFoundException.class);
        verify(queryMusicPort).existsById(TEST_MUSIC_ID);
        verify(saveMusicLikePort, never()).save(any(MusicLike.class));
        verify(deleteMusicLikePort, never()).deleteByUserIdAndMusicId(any(), any());
    }

    @Test
    @DisplayName("다른 음악에 대해서도 좋아요를 추가할 수 있다")
    void whenDifferentMusic_thenAddsLike() {
        // given
        Long differentMusicId = 20L;
        User user = createUser();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMusicPort.existsById(differentMusicId)).willReturn(true);
        given(queryMusicLikePort.existsByUserIdAndMusicId(TEST_USER_ID, differentMusicId))
                .willReturn(false);

        // when
        toggleMusicLikeService.execute(differentMusicId);

        // then
        verify(saveMusicLikePort).save(any(MusicLike.class));
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
