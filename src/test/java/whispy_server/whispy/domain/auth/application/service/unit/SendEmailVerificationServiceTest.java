package whispy_server.whispy.domain.auth.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.SendEmailVerificationRequest;
import whispy_server.whispy.domain.auth.application.port.out.EmailSendPort;
import whispy_server.whispy.domain.auth.application.service.SendEmailVerificationService;
import whispy_server.whispy.global.exception.domain.auth.EmailAlreadySentException;
import whispy_server.whispy.global.exception.domain.auth.EmailRateLimitExceededException;
import whispy_server.whispy.global.exception.domain.auth.EmailSendFailedException;
import whispy_server.whispy.global.utils.redis.RedisUtil;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * SendEmailVerificationService의 단위 테스트 클래스
 * <p>
 * 이메일 인증 코드 발송 서비스의 다양한 시나리오를 검증합니다.
 * Rate limit, 중복 요청 검증, 인증 코드 생성 및 발송, 예외 처리 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SendEmailVerificationService 테스트")
class SendEmailVerificationServiceTest {

    @InjectMocks
    private SendEmailVerificationService sendEmailVerificationService;

    @Mock
    private RedisUtil redisUtil;

    @Mock
    private EmailSendPort emailSendPort;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String VERIFICATION_CODE_KEY = "email:verification:code:";
    private static final String RATE_LIMIT_KEY = "email:rate:limit:";
    private static final Duration CODE_EXPIRATION = Duration.ofMinutes(5);
    private static final Duration RATE_LIMIT_DURATION = Duration.ofMinutes(1);

    @Test
    @DisplayName("정상적으로 인증 코드를 발송할 수 있다")
    void whenValidRequest_thenSendsVerificationCode() {
        // given
        SendEmailVerificationRequest request = new SendEmailVerificationRequest(TEST_EMAIL);
        String codeKey = VERIFICATION_CODE_KEY + TEST_EMAIL;
        String rateLimitKey = RATE_LIMIT_KEY + TEST_EMAIL;

        given(redisUtil.setIfAbsent(eq(rateLimitKey), eq("sent"), eq(RATE_LIMIT_DURATION)))
                .willReturn(true);
        given(redisUtil.setIfAbsent(eq(codeKey), anyString(), eq(CODE_EXPIRATION)))
                .willReturn(true);

        // when
        sendEmailVerificationService.execute(request);

        // then
        verify(redisUtil).setIfAbsent(eq(rateLimitKey), eq("sent"), eq(RATE_LIMIT_DURATION));
        verify(redisUtil).setIfAbsent(eq(codeKey), anyString(), eq(CODE_EXPIRATION));
        verify(emailSendPort).sendVerificationCode(eq(TEST_EMAIL), anyString());
    }

    @Test
    @DisplayName("Rate limit을 초과하면 예외가 발생한다")
    void whenRateLimitExceeded_thenThrowsException() {
        // given
        SendEmailVerificationRequest request = new SendEmailVerificationRequest(TEST_EMAIL);
        String rateLimitKey = RATE_LIMIT_KEY + TEST_EMAIL;

        given(redisUtil.setIfAbsent(eq(rateLimitKey), eq("sent"), eq(RATE_LIMIT_DURATION)))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> sendEmailVerificationService.execute(request))
                .isInstanceOf(EmailRateLimitExceededException.class);
        verify(redisUtil).setIfAbsent(eq(rateLimitKey), eq("sent"), eq(RATE_LIMIT_DURATION));
        verify(emailSendPort, never()).sendVerificationCode(anyString(), anyString());
    }

    @Test
    @DisplayName("이미 발송된 인증 코드가 있으면 예외가 발생한다")
    void whenCodeAlreadySent_thenThrowsException() {
        // given
        SendEmailVerificationRequest request = new SendEmailVerificationRequest(TEST_EMAIL);
        String codeKey = VERIFICATION_CODE_KEY + TEST_EMAIL;
        String rateLimitKey = RATE_LIMIT_KEY + TEST_EMAIL;

        given(redisUtil.setIfAbsent(eq(rateLimitKey), eq("sent"), eq(RATE_LIMIT_DURATION)))
                .willReturn(true);
        given(redisUtil.setIfAbsent(eq(codeKey), anyString(), eq(CODE_EXPIRATION)))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> sendEmailVerificationService.execute(request))
                .isInstanceOf(EmailAlreadySentException.class);
        verify(redisUtil).delete(rateLimitKey);
        verify(emailSendPort, never()).sendVerificationCode(anyString(), anyString());
    }

    @Test
    @DisplayName("이메일 발송 실패 시 Redis 데이터를 롤백하고 예외가 발생한다")
    void whenEmailSendFails_thenRollbacksAndThrowsException() {
        // given
        SendEmailVerificationRequest request = new SendEmailVerificationRequest(TEST_EMAIL);
        String codeKey = VERIFICATION_CODE_KEY + TEST_EMAIL;
        String rateLimitKey = RATE_LIMIT_KEY + TEST_EMAIL;

        given(redisUtil.setIfAbsent(eq(rateLimitKey), eq("sent"), eq(RATE_LIMIT_DURATION)))
                .willReturn(true);
        given(redisUtil.setIfAbsent(eq(codeKey), anyString(), eq(CODE_EXPIRATION)))
                .willReturn(true);
        doThrow(new RuntimeException("Email send failed"))
                .when(emailSendPort).sendVerificationCode(eq(TEST_EMAIL), anyString());

        // when & then
        assertThatThrownBy(() -> sendEmailVerificationService.execute(request))
                .isInstanceOf(EmailSendFailedException.class);
        verify(redisUtil).delete(codeKey);
        verify(redisUtil).delete(rateLimitKey);
    }

    @Test
    @DisplayName("다른 이메일 주소로도 인증 코드를 발송할 수 있다")
    void whenDifferentEmail_thenSendsSuccessfully() {
        // given
        String differentEmail = "other@example.com";
        SendEmailVerificationRequest request = new SendEmailVerificationRequest(differentEmail);
        String codeKey = VERIFICATION_CODE_KEY + differentEmail;
        String rateLimitKey = RATE_LIMIT_KEY + differentEmail;

        given(redisUtil.setIfAbsent(eq(rateLimitKey), eq("sent"), eq(RATE_LIMIT_DURATION)))
                .willReturn(true);
        given(redisUtil.setIfAbsent(eq(codeKey), anyString(), eq(CODE_EXPIRATION)))
                .willReturn(true);

        // when
        sendEmailVerificationService.execute(request);

        // then
        verify(emailSendPort).sendVerificationCode(eq(differentEmail), anyString());
    }
}
