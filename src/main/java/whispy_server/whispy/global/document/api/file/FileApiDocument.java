package whispy_server.whispy.global.document.api.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.domain.file.adapter.in.web.dto.FileUploadResponse;
import whispy_server.whispy.domain.file.type.ImageFolder;

@Tag(name = "FILE API", description = "파일 관련 API")
public interface FileApiDocument {

    @Operation(summary = "파일 업로드", description = "지정된 폴더에 파일을 업로드합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "파일 업로드 성공",
                    content = @Content(schema = @Schema(implementation = FileUploadResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 파일 형식 오류"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "413", description = "파일 크기 초과"),
            @ApiResponse(responseCode = "415", description = "지원하지 않는 파일 형식"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    FileUploadResponse uploadFile(
            @Parameter(description = "업로드할 파일", required = true) MultipartFile file,
            @Parameter(description = "파일을 저장할 폴더", required = true) ImageFolder folder
    );

    @Operation(summary = "파일 삭제", description = "지정된 폴더에서 파일을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "파일 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "파일 삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    void deleteFile(
            @Parameter(description = "파일이 위치한 폴더", required = true) ImageFolder folder,
            @Parameter(description = "삭제할 파일명", required = true) String fileName
    );
}
