package whispy_server.whispy.domain.file.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "파일 업로드 응답")
public record FileUploadResponse(
        @Schema(description = "업로드된 파일 URL", example = "https://storage.googleapis.com/bucket/file.jpg")
        String fileUrl
) {
}
