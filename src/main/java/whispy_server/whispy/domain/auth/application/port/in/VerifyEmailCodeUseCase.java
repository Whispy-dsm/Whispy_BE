package whispy_server.whispy.domain.auth.application.port.in;

import whispy_server.whispy.domain.auth.adapter.in.dto.request.VerifyEmailCodeRequest;
import whispy_server.whispy.domain.auth.adapter.in.dto.response.VerifyEmailCodeResponse;
import whispy_server.whispy.global.annotation.UseCase;

@UseCase
public interface VerifyEmailCodeUseCase {
    VerifyEmailCodeResponse execute(VerifyEmailCodeRequest request);
}
