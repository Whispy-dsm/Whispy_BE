package whispy_server.whispy.global.document.api.announcement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAllAnnouncementResponse;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.response.QueryAnnouncementResponse;

import java.util.List;

@Tag(name = "ANNOUNCEMENT API", description = "공지사항 관련 API")
public interface AnnouncementApiDocument {

    @Operation(summary = "공지사항 단건 조회", description = "ID로 특정 공지사항을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 조회 성공",
                    content = @Content(schema = @Schema(implementation = QueryAnnouncementResponse.class))),
            @ApiResponse(responseCode = "404", description = "공지사항을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    QueryAnnouncementResponse getAnnouncement(
            @Parameter(description = "공지사항 ID", required = true) Long id
    );

    @Operation(summary = "공지사항 전체 조회", description = "모든 공지사항을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = QueryAllAnnouncementResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    List<QueryAllAnnouncementResponse> getAllAnnouncements();
}
