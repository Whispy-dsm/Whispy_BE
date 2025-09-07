package whispy_server.whispy.domain.auth.application.port.in;

import whispy_server.whispy.domain.auth.adapter.in.dto.request.CheckEmailVerificationRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.CheckEmailVerificationResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface CheckEmailVerificationUseCase {
    CheckEmailVerificationResponse execute(CheckEmailVerificationRequest request);
}
