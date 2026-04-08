package whispy_server.whispy.global.document.api.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import whispy_server.whispy.global.exception.error.ErrorResponse;

/**
 * 공개 파일 조회 엔드포인트의 Swagger 설명을 담는 인터페이스입니다.
 *
 * 파일 조회 컨트롤러는 이 인터페이스를 구현하여
 * 기존 `/file/**` 공개 URL의 문서화 규칙을 일관되게 유지합니다.
 */
@Tag(name = "FILE API", description = "파일 관련 API")
public interface FileAssetApiDocument {

    @Operation(
            summary = "공개 파일 조회",
            description = "기존 공개 파일 URL(`/file/{folder}/{fileName}`)로 업로드된 파일을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "파일 조회 성공",
                    content = @Content(mediaType = "*/*", schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "206", description = "부분 파일 조회 성공",
                    content = @Content(mediaType = "*/*", schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "416", description = "처리할 수 없는 파일 범위 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<InputStreamResource> getFile(
            @Parameter(description = "파일이 속한 폴더 경로", required = true) String folder,
            @Parameter(description = "조회할 파일명", required = true) String fileName,
            @Parameter(description = "부분 다운로드를 위한 HTTP Range 헤더", required = false) String rangeHeader
    );
}
