package whispy_server.whispy.domain.auth.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.VerifyEmailCodeRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.VerifyEmailCodeResponse;
import whispy_server.whispy.domain.auth.application.service.VerifyEmailCodeService;
import whispy_server.whispy.global.utils.redis.RedisUtil;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * VerifyEmailCodeService의 단위 테스트 클래스
 *
 * 이메일 인증 코드 검증 서비스의 다양한 시나리오를 검증합니다.
 * Redis에서 코드를 조회하여 일치 여부를 판단하고, 인증 상태를 기록하는 로직을 테스트합니다.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("VerifyEmailCodeService 테스트")
class VerifyEmailCodeServiceTest {

    @InjectMocks
    private VerifyEmailCodeService verifyEmailCodeService;

    @Mock
    private RedisUtil redisUtil;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_CODE = "123456";
    private static final String VERIFICATION_CODE_KEY = "email:verification:code:";
    private static final String VERIFICATION_STATUS_KEY = "email:verification:status:";
    private static final Duration STATUS_EXPIRATION = Duration.ofMinutes(10);

    @Test
    @DisplayName("올바른 인증 코드로 검증에 성공한다")
    void whenCorrectCode_thenVerificationSucceeds() {
        // given
        VerifyEmailCodeRequest request = new VerifyEmailCodeRequest(TEST_EMAIL, TEST_CODE);
        String codeKey = VERIFICATION_CODE_KEY + TEST_EMAIL;
        String statusKey = VERIFICATION_STATUS_KEY + TEST_EMAIL;

        given(redisUtil.get(codeKey)).willReturn(TEST_CODE);

        // when
        VerifyEmailCodeResponse response = verifyEmailCodeService.execute(request);

        // then
        assertThat(response.isVerified()).isTrue();
        verify(redisUtil).get(codeKey);
        verify(redisUtil).set(statusKey, "verified", STATUS_EXPIRATION);
        verify(redisUtil).delete(codeKey);
    }

    @Test
    @DisplayName("잘못된 인증 코드로 검증에 실패한다")
    void whenIncorrectCode_thenVerificationFails() {
        // given
        VerifyEmailCodeRequest request = new VerifyEmailCodeRequest(TEST_EMAIL, "wrong123");
        String codeKey = VERIFICATION_CODE_KEY + TEST_EMAIL;
        String statusKey = VERIFICATION_STATUS_KEY + TEST_EMAIL;

        given(redisUtil.get(codeKey)).willReturn(TEST_CODE);

        // when
        VerifyEmailCodeResponse response = verifyEmailCodeService.execute(request);

        // then
        assertThat(response.isVerified()).isFalse();
        verify(redisUtil).get(codeKey);
        verify(redisUtil, never()).set(eq(statusKey), eq("verified"), eq(STATUS_EXPIRATION));
        verify(redisUtil, never()).delete(codeKey);
    }

    @Test
    @DisplayName("존재하지 않는 인증 코드로 검증에 실패한다")
    void whenCodeNotFound_thenVerificationFails() {
        // given
        VerifyEmailCodeRequest request = new VerifyEmailCodeRequest(TEST_EMAIL, TEST_CODE);
        String codeKey = VERIFICATION_CODE_KEY + TEST_EMAIL;
        String statusKey = VERIFICATION_STATUS_KEY + TEST_EMAIL;

        given(redisUtil.get(codeKey)).willReturn(null);

        // when
        VerifyEmailCodeResponse response = verifyEmailCodeService.execute(request);

        // then
        assertThat(response.isVerified()).isFalse();
        verify(redisUtil).get(codeKey);
        verify(redisUtil, never()).set(eq(statusKey), eq("verified"), eq(STATUS_EXPIRATION));
        verify(redisUtil, never()).delete(codeKey);
    }

    @Test
    @DisplayName("다른 이메일 주소로도 인증 코드를 검증할 수 있다")
    void whenDifferentEmail_thenVerifiesSuccessfully() {
        // given
        String differentEmail = "other@example.com";
        VerifyEmailCodeRequest request = new VerifyEmailCodeRequest(differentEmail, TEST_CODE);
        String codeKey = VERIFICATION_CODE_KEY + differentEmail;
        String statusKey = VERIFICATION_STATUS_KEY + differentEmail;

        given(redisUtil.get(codeKey)).willReturn(TEST_CODE);

        // when
        VerifyEmailCodeResponse response = verifyEmailCodeService.execute(request);

        // then
        assertThat(response.isVerified()).isTrue();
        verify(redisUtil).set(statusKey, "verified", STATUS_EXPIRATION);
        verify(redisUtil).delete(codeKey);
    }

    @Test
    @DisplayName("검증 성공 시 기존 인증 코드를 삭제한다")
    void whenVerificationSucceeds_thenDeletesCode() {
        // given
        VerifyEmailCodeRequest request = new VerifyEmailCodeRequest(TEST_EMAIL, TEST_CODE);
        String codeKey = VERIFICATION_CODE_KEY + TEST_EMAIL;

        given(redisUtil.get(codeKey)).willReturn(TEST_CODE);

        // when
        verifyEmailCodeService.execute(request);

        // then
        verify(redisUtil).delete(codeKey);
    }

    @Test
    @DisplayName("검증 성공 시 인증 상태를 저장한다")
    void whenVerificationSucceeds_thenSavesStatus() {
        // given
        VerifyEmailCodeRequest request = new VerifyEmailCodeRequest(TEST_EMAIL, TEST_CODE);
        String codeKey = VERIFICATION_CODE_KEY + TEST_EMAIL;
        String statusKey = VERIFICATION_STATUS_KEY + TEST_EMAIL;

        given(redisUtil.get(codeKey)).willReturn(TEST_CODE);

        // when
        verifyEmailCodeService.execute(request);

        // then
        verify(redisUtil).set(statusKey, "verified", STATUS_EXPIRATION);
    }
}
