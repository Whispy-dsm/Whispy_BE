package whispy_server.whispy.domain.auth.application.port.in;

import whispy_server.whispy.domain.auth.adapter.in.dto.request.SendEmailVerificationRequest;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface SendEmailVerificationUseCase {
    void execute(SendEmailVerificationRequest request);
}
