package whispy_server.whispy.domain.payment.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "PubSub 메시지 데이터 요청")
public record PubSubMessageDataRequest(
        @Schema(description = "메시지 속성")
        Map<String, String> attributes,
        @Schema(description = "메시지 데이터 (Base64 인코딩)", example = "eyJkYXRhIjoiZXhhbXBsZSJ9")
        String data,
        @Schema(description = "메시지 ID", example = "1234567890")
        String messageId
) {}
