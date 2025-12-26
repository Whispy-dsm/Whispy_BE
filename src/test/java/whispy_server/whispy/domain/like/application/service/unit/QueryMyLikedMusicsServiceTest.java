package whispy_server.whispy.domain.like.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.like.adapter.in.web.dto.response.LikedMusicResponse;
import whispy_server.whispy.domain.like.adapter.out.dto.MusicLikeWithMusicDto;
import whispy_server.whispy.domain.like.application.port.out.QueryMusicLikePort;
import whispy_server.whispy.domain.like.application.service.QueryMyLikedMusicsService;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * QueryMyLikedMusicsService의 단위 테스트 클래스
 *
 * 좋아요한 음악 목록 조회 서비스의 다양한 시나리오를 검증합니다.
 * 사용자가 좋아요한 음악 목록을 조회하는 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("QueryMyLikedMusicsService 테스트")
class QueryMyLikedMusicsServiceTest {

    @InjectMocks
    private QueryMyLikedMusicsService queryMyLikedMusicsService;

    @Mock
    private QueryMusicLikePort queryMusicLikePort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("좋아요한 음악 목록을 조회할 수 있다")
    void whenQueryingLikedMusics_thenReturnsLikedList() {
        // given
        User user = createUser();
        List<MusicLikeWithMusicDto> likedMusics = List.of(
                createMusicLikeDto(1L, 10L, "음악 1"),
                createMusicLikeDto(2L, 20L, "음악 2"),
                createMusicLikeDto(3L, 30L, "음악 3")
        );

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMusicLikePort.findLikedMusicsWithDetailByUserId(TEST_USER_ID))
                .willReturn(likedMusics);

        // when
        List<LikedMusicResponse> result = queryMyLikedMusicsService.execute();

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        verify(userFacadeUseCase).currentUser();
        verify(queryMusicLikePort).findLikedMusicsWithDetailByUserId(TEST_USER_ID);
    }

    @Test
    @DisplayName("좋아요한 음악이 없을 때 빈 리스트를 반환한다")
    void whenNoLikedMusics_thenReturnsEmptyList() {
        // given
        User user = createUser();
        List<MusicLikeWithMusicDto> emptyList = List.of();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMusicLikePort.findLikedMusicsWithDetailByUserId(TEST_USER_ID))
                .willReturn(emptyList);

        // when
        List<LikedMusicResponse> result = queryMyLikedMusicsService.execute();

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("많은 수의 좋아요 음악도 조회할 수 있다")
    void whenManyLikedMusics_thenReturnsAll() {
        // given
        User user = createUser();
        List<MusicLikeWithMusicDto> manyLikedMusics = java.util.stream.IntStream.range(0, 50)
                .mapToObj(i -> createMusicLikeDto((long) i, (long) (i + 100), "음악 " + i))
                .toList();

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(queryMusicLikePort.findLikedMusicsWithDetailByUserId(TEST_USER_ID))
                .willReturn(manyLikedMusics);

        // when
        List<LikedMusicResponse> result = queryMyLikedMusicsService.execute();

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(50);
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

    /**
     * 테스트용 MusicLikeWithMusicDto 객체를 생성합니다.
     *
     * @param likeId 좋아요 ID
     * @param musicId 음악 ID
     * @param musicTitle 음악 제목
     * @return 생성된 MusicLikeWithMusicDto 객체
     */
    private MusicLikeWithMusicDto createMusicLikeDto(Long likeId, Long musicId, String musicTitle) {
        return new MusicLikeWithMusicDto(
                musicId,
                musicTitle,
                "http://example.com/music.mp3",
                180,
                whispy_server.whispy.domain.music.model.type.MusicCategory.NATURE
        );
    }
}
