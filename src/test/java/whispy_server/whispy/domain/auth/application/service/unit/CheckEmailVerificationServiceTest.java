package whispy_server.whispy.domain.auth.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.CheckEmailVerificationRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.CheckEmailVerificationResponse;
import whispy_server.whispy.domain.auth.application.service.CheckEmailVerificationService;
import whispy_server.whispy.global.utils.redis.RedisUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * CheckEmailVerificationService의 단위 테스트 클래스
 * <p>
 * 이메일 인증 상태 확인 서비스의 다양한 시나리오를 검증합니다.
 * Redis에서 인증 상태를 조회하여 인증 완료 여부를 반환하는 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CheckEmailVerificationService 테스트")
class CheckEmailVerificationServiceTest {

    @InjectMocks
    private CheckEmailVerificationService checkEmailVerificationService;

    @Mock
    private RedisUtil redisUtil;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String VERIFICATION_STATUS_KEY = "email:verification:status:";

    @Test
    @DisplayName("인증 완료된 이메일은 true를 반환한다")
    void whenEmailIsVerified_thenReturnsTrue() {
        // given
        CheckEmailVerificationRequest request = new CheckEmailVerificationRequest(TEST_EMAIL);
        String statusKey = VERIFICATION_STATUS_KEY + TEST_EMAIL;

        given(redisUtil.get(statusKey)).willReturn("verified");

        // when
        CheckEmailVerificationResponse response = checkEmailVerificationService.execute(request);

        // then
        assertThat(response.isVerified()).isTrue();
        verify(redisUtil).get(statusKey);
    }

    @Test
    @DisplayName("인증되지 않은 이메일은 false를 반환한다")
    void whenEmailIsNotVerified_thenReturnsFalse() {
        // given
        CheckEmailVerificationRequest request = new CheckEmailVerificationRequest(TEST_EMAIL);
        String statusKey = VERIFICATION_STATUS_KEY + TEST_EMAIL;

        given(redisUtil.get(statusKey)).willReturn(null);

        // when
        CheckEmailVerificationResponse response = checkEmailVerificationService.execute(request);

        // then
        assertThat(response.isVerified()).isFalse();
        verify(redisUtil).get(statusKey);
    }

    @Test
    @DisplayName("잘못된 상태 값을 가진 이메일은 false를 반환한다")
    void whenInvalidStatus_thenReturnsFalse() {
        // given
        CheckEmailVerificationRequest request = new CheckEmailVerificationRequest(TEST_EMAIL);
        String statusKey = VERIFICATION_STATUS_KEY + TEST_EMAIL;

        given(redisUtil.get(statusKey)).willReturn("pending");

        // when
        CheckEmailVerificationResponse response = checkEmailVerificationService.execute(request);

        // then
        assertThat(response.isVerified()).isFalse();
        verify(redisUtil).get(statusKey);
    }

    @Test
    @DisplayName("다른 이메일 주소도 정상적으로 확인할 수 있다")
    void whenDifferentEmail_thenChecksSuccessfully() {
        // given
        String differentEmail = "other@example.com";
        CheckEmailVerificationRequest request = new CheckEmailVerificationRequest(differentEmail);
        String statusKey = VERIFICATION_STATUS_KEY + differentEmail;

        given(redisUtil.get(statusKey)).willReturn("verified");

        // when
        CheckEmailVerificationResponse response = checkEmailVerificationService.execute(request);

        // then
        assertThat(response.isVerified()).isTrue();
        verify(redisUtil).get(statusKey);
    }
}
