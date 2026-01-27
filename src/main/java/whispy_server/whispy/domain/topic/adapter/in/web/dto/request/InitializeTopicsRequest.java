package whispy_server.whispy.domain.topic.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 토픽 초기화 요청 DTO.
 */
@Schema(description = "토픽 초기화 요청")
public record InitializeTopicsRequest(
        @Schema(description = "사용자 이메일", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Email
        String email,

        @Schema(description = "FCM 토큰", example = "fGcm_T0k3n_ExAmPlE...", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String fcmToken,

        @Schema(description = "이벤트 수신 동의 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Boolean isEventAgreed
) {
    @Override
    public String toString() {
        return "InitializeTopicsRequest[email=***, fcmToken=***, isEventAgreed=" + isEventAgreed + "]";
    }
}
