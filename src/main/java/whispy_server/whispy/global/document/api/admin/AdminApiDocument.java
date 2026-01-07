package whispy_server.whispy.global.document.api.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.admin.adapter.in.web.dto.request.AdminLoginRequest;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.CreateAnnouncementRequest;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.UpdateAnnouncementRequest;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonDetailResponse;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonSummaryResponse;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonsByDateResponse;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalStatisticsByDateResponse;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.AddNewTopicRequest;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.exception.error.ErrorResponse;

import java.time.LocalDate;
import java.util.List;

import static whispy_server.whispy.global.config.swagger.SwaggerConfig.SECURITY_SCHEME_NAME;

/**
 * 관리자 전용 기능(음악/공지/토픽/통계 관리 등)을 문서화하는 Swagger 인터페이스이다.
 * 어드민 컨트롤러가 이 계약을 구현해 공통 API 설명과 보안 요구사항을 공유한다.
 */
@Tag(name = "ADMIN API", description = "관리자 API")
public interface AdminApiDocument {

    @Operation(
            summary = "관리자 로그인",
            description = "관리자 계정으로 로그인합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    TokenResponse login(
            @RequestBody(description = "관리자 로그인 요청", required = true,
                    content = @Content(schema = @Schema(implementation = AdminLoginRequest.class)))
            AdminLoginRequest request
    );

    @Operation(
            summary = "관리자 로그아웃",
            description = "현재 인증된 관리자를 로그아웃합니다. Redis에서 리프레시 토큰을 삭제합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void logout();

    @Operation(
            summary = "모든 유저에게 새 토픽 추가",
            description = "모든 유저에게 새로운 토픽을 추가합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "토픽 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void addNewTopicToAllUsers(
            @RequestBody(description = "새 토픽 추가 요청", required = true,
                    content = @Content(schema = @Schema(implementation = AddNewTopicRequest.class)))
            AddNewTopicRequest request
    );

    @Operation(
            summary = "음악 생성",
            description = "새로운 음악을 생성합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "음악 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void createMusic(
            @RequestBody(description = "음악 생성 요청", required = true,
                    content = @Content(schema = @Schema(implementation = CreateMusicRequest.class)))
            CreateMusicRequest request
    );

    @Operation(
            summary = "음악 수정",
            description = "기존 음악 정보를 수정합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "음악 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void updateMusic(
            @RequestBody(description = "음악 수정 요청", required = true,
                    content = @Content(schema = @Schema(implementation = UpdateMusicRequest.class)))
            UpdateMusicRequest request
    );

    @Operation(
            summary = "음악 삭제",
            description = "ID로 음악을 삭제합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "음악 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "음악을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void deleteMusic(
            @Parameter(description = "음악 ID", required = true) Long id
    );

    @Operation(
            summary = "공지사항 생성",
            description = "새로운 공지사항을 생성합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "공지사항 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void createAnnouncement(
            @RequestBody(description = "공지사항 생성 요청", required = true,
                    content = @Content(schema = @Schema(implementation = CreateAnnouncementRequest.class)))
            CreateAnnouncementRequest request
    );

    @Operation(
            summary = "공지사항 수정",
            description = "기존 공지사항을 수정합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "공지사항 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "공지사항을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void updateAnnouncement(
            @RequestBody(description = "공지사항 수정 요청", required = true,
                    content = @Content(schema = @Schema(implementation = UpdateAnnouncementRequest.class)))
            UpdateAnnouncementRequest request
    );

    @Operation(
            summary = "공지사항 삭제",
            description = "ID로 공지사항을 삭제합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "공지사항 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "공지사항을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void deleteAnnouncement(
            @Parameter(description = "공지사항 ID", required = true) Long id
    );

    @Operation(
            summary = "탈퇴 사유 목록 조회",
            description = "모든 탈퇴 사유를 페이징하여 조회합니다. (상세 내용 제외)",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴 사유 조회 성공",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    Page<WithdrawalReasonSummaryResponse> getWithdrawalReasons(Pageable pageable);

    @Operation(
            summary = "탈퇴 사유 상세 조회",
            description = "ID로 탈퇴 사유의 상세 정보를 조회합니다. (상세 내용 포함)",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴 사유 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = WithdrawalReasonDetailResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "탈퇴 사유를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    WithdrawalReasonDetailResponse getWithdrawalReasonDetail(
            @Parameter(description = "탈퇴 사유 ID", required = true) Long id
    );

    @Operation(
            summary = "탈퇴 사유 삭제",
            description = "ID로 탈퇴 사유를 삭제합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "탈퇴 사유 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "탈퇴 사유를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void deleteWithdrawalReason(
            @Parameter(description = "탈퇴 사유 ID", required = true) Long id
    );

    @Operation(
            summary = "날짜별 탈퇴 사유 목록 조회",
            description = "특정 날짜의 탈퇴 사유 목록을 페이징하여 조회합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "날짜별 탈퇴 사유 조회 성공",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    Page<WithdrawalReasonsByDateResponse> getWithdrawalReasonsByDate(
            @Parameter(description = "조회할 날짜 (yyyy-MM-dd)", required = true, example = "2024-01-15")
            LocalDate date,
            Pageable pageable
    );

    @Operation(
            summary = "날짜별 탈퇴 통계 조회",
            description = "기간 내 날짜별 탈퇴 건수를 조회합니다. 그래프 표시를 위한 통계 데이터를 제공합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴 통계 조회 성공",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    List<WithdrawalStatisticsByDateResponse> getWithdrawalStatisticsByDate(
            @Parameter(description = "시작 날짜 (yyyy-MM-dd)", required = true, example = "2024-01-01")
            LocalDate startDate,
            @Parameter(description = "종료 날짜 (yyyy-MM-dd)", required = true, example = "2024-01-31")
            LocalDate endDate
    );

    @Operation(
            summary = "디바이스 토큰으로 알림 전송",
            description = "관리자가 특정 디바이스 토큰들에게 알림을 전송합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "알림 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void sendNotification(
            @RequestBody(description = "알림 전송 요청", required = true,
                    content = @Content(schema = @Schema(implementation = NotificationSendRequest.class)))
            NotificationSendRequest request
    );

    @Operation(
            summary = "토픽으로 알림 전송",
            description = "관리자가 특정 토픽을 구독한 사용자들에게 알림을 전송합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "토픽 알림 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void sendToTopic(
            @RequestBody(description = "토픽 알림 전송 요청", required = true,
                    content = @Content(schema = @Schema(implementation = NotificationTopicSendRequest.class)))
            NotificationTopicSendRequest request
    );

    @Operation(
            summary = "전체 사용자에게 브로드캐스트",
            description = "관리자가 모든 사용자에게 알림을 브로드캐스트합니다.",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "브로드캐스트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검사 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    void broadcastToAllUsers(
            @RequestBody(description = "브로드캐스트 요청", required = true,
                    content = @Content(schema = @Schema(implementation = NotificationTopicSendRequest.class)))
            NotificationTopicSendRequest request
    );
}
