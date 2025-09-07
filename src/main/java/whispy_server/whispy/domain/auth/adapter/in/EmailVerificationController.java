package whispy_server.whispy.domain.auth.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class EmailVerificationController implements EmailVerificationApiDocument {

    private final SendEmailVerificationUseCase sendEmailVerificationUseCase;
    private final VerifyEmailCodeUseCase verifyEmailCodeUseCase;
    private final CheckEmailVerificationUseCase checkEmailVerificationUseCase;

    @PostMapping("/email/send")
    public void sendVerificationCode(@Valid @RequestBody SendEmailVerificationRequest request) {
        sendEmailVerificationUseCase.execute(request);
    }

    @PostMapping("/email/verify")
    public VerifyEmailCodeResponse verifyCode(@Valid @RequestBody VerifyEmailCodeRequest request) {
        return verifyEmailCodeUseCase.execute(request);
    }

    @PostMapping("/email/status")
    public CheckEmailVerificationResponse checkVerificationStatus(@Valid @RequestBody CheckEmailVerificationRequest request) {
        return checkEmailVerificationUseCase.execute(request);
    }
}
