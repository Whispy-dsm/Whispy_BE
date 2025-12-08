package whispy_server.whispy.domain.auth.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.CheckEmailVerificationRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.SendEmailVerificationRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.request.VerifyEmailCodeRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.CheckEmailVerificationResponse;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.VerifyEmailCodeResponse;
import whispy_server.whispy.domain.auth.application.port.in.CheckEmailVerificationUseCase;
import whispy_server.whispy.domain.auth.application.port.in.SendEmailVerificationUseCase;
import whispy_server.whispy.domain.auth.application.port.in.VerifyEmailCodeUseCase;
import whispy_server.whispy.global.document.api.auth.EmailVerificationApiDocument;

/**
 * 이메일 인증 REST 컨트롤러
 *
 * 이메일 인증 관련 HTTP 엔드포인트를 제공하는 인바운드 어댑터입니다.
 * 인증 코드 발송, 인증 코드 검증, 인증 상태 확인 기능을 제공합니다.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class EmailVerificationController implements EmailVerificationApiDocument {

    private final SendEmailVerificationUseCase sendEmailVerificationUseCase;
    private final VerifyEmailCodeUseCase verifyEmailCodeUseCase;
    private final CheckEmailVerificationUseCase checkEmailVerificationUseCase;

    /**
     * 이메일로 인증 코드를 발송합니다.
     *
     * 6자리 숫자 인증 코드를 생성하여 지정된 이메일로 발송합니다.
     * Rate limit(1분) 및 중복 요청(5분) 검증이 적용됩니다.
     *
     * @param request 인증 코드를 받을 이메일 주소가 포함된 요청
     */
    @PostMapping("/email/send")
    public void sendVerificationCode(@Valid @RequestBody SendEmailVerificationRequest request) {
        sendEmailVerificationUseCase.execute(request);
    }

    /**
     * 이메일 인증 코드를 검증합니다.
     *
     * 사용자가 입력한 인증 코드가 발송된 코드와 일치하는지 확인합니다.
     * 검증 성공 시 인증 상태를 Redis에 10분간 저장합니다.
     *
     * @param request 이메일 주소와 인증 코드가 포함된 요청
     * @return 인증 성공 여부가 포함된 응답
     */
    @PostMapping("/email/verify")
    public VerifyEmailCodeResponse verifyCode(@Valid @RequestBody VerifyEmailCodeRequest request) {
        return verifyEmailCodeUseCase.execute(request);
    }

    /**
     * 이메일 인증 상태를 확인합니다.
     *
     * 주어진 이메일이 인증 완료 상태인지 조회합니다.
     * Redis에 저장된 인증 상태 값을 확인하여 결과를 반환합니다.
     *
     * @param request 인증 상태를 확인할 이메일 주소가 포함된 요청
     * @return 인증 완료 여부가 포함된 응답
     */
    @PostMapping("/email/status")
    public CheckEmailVerificationResponse checkVerificationStatus(@Valid @RequestBody CheckEmailVerificationRequest request) {
        return checkEmailVerificationUseCase.execute(request);
    }
}
