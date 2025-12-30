package whispy_server.whispy.domain.admin.adapter.in.web;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.admin.adapter.in.web.dto.request.AdminLoginRequest;
import whispy_server.whispy.domain.admin.application.port.in.AdminLoginUseCase;
import whispy_server.whispy.domain.admin.application.port.in.AdminLogoutUseCase;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.CreateAnnouncementRequest;
import whispy_server.whispy.domain.announcement.adapter.in.web.dto.request.UpdateAnnouncementRequest;
import whispy_server.whispy.domain.announcement.application.port.in.CreateAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.in.DeleteAnnouncementUseCase;
import whispy_server.whispy.domain.announcement.application.port.in.UpdateAnnouncementUseCase;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.CreateMusicRequest;
import whispy_server.whispy.domain.music.adapter.in.web.dto.request.UpdateMusicRequest;
import whispy_server.whispy.domain.music.application.port.in.CreateMusicUseCase;
import whispy_server.whispy.domain.music.application.port.in.DeleteMusicUseCase;
import whispy_server.whispy.domain.music.application.port.in.UpdateMusicUseCase;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationSendRequest;
import whispy_server.whispy.domain.notification.adapter.in.web.dto.request.NotificationTopicSendRequest;
import whispy_server.whispy.domain.notification.application.port.in.BroadCastToAllUsersUseCase;
import whispy_server.whispy.domain.notification.application.port.in.SendToDeviceTokensUseCase;
import whispy_server.whispy.domain.notification.application.port.in.SendToTopicUseCase;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonDetailResponse;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonResponse;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonSummaryResponse;
import whispy_server.whispy.domain.reason.application.port.in.DeleteWithdrawalReasonUseCase;
import whispy_server.whispy.domain.reason.application.port.in.GetWithdrawalReasonDetailUseCase;
import whispy_server.whispy.domain.reason.application.port.in.GetWithdrawalReasonsUseCase;
import whispy_server.whispy.domain.topic.adapter.in.web.dto.request.AddNewTopicRequest;
import whispy_server.whispy.domain.topic.application.port.in.AddNewTopicForAllUsersUseCase;
import whispy_server.whispy.domain.user.adapter.in.web.dto.response.TokenResponse;
import whispy_server.whispy.global.document.api.admin.AdminApiDocument;

/**
 * 관리자 REST 컨트롤러.
 *
 * 관리자 전용 기능을 제공하는 인바운드 어댑터입니다.
 * 음악, 공지사항, 토픽, 알림 관리 및 회원 탈퇴 사유 조회 기능을 제공합니다.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController implements AdminApiDocument {

    private final AddNewTopicForAllUsersUseCase addNewTopicForAllUsersUseCase;
    private final CreateMusicUseCase createMusicUseCase;
    private final UpdateMusicUseCase updateMusicUseCase;
    private final DeleteMusicUseCase deleteMusicUseCase;
    private final CreateAnnouncementUseCase createAnnouncementUseCase;
    private final UpdateAnnouncementUseCase updateAnnouncementUseCase;
    private final DeleteAnnouncementUseCase deleteAnnouncementUseCase;
    private final AdminLoginUseCase adminLoginUseCase;
    private final AdminLogoutUseCase adminLogoutUseCase;
    private final GetWithdrawalReasonsUseCase getWithdrawalReasonsUseCase;
    private final GetWithdrawalReasonDetailUseCase getWithdrawalReasonDetailUseCase;
    private final DeleteWithdrawalReasonUseCase deleteWithdrawalReasonUseCase;
    private final SendToDeviceTokensUseCase sendToDeviceTokensUseCase;
    private final SendToTopicUseCase sendToTopicUseCase;
    private final BroadCastToAllUsersUseCase broadCastToAllUsersUseCase;

    /**
     * 관리자 로그인을 처리합니다.
     *
     * @param request 관리자 ID와 비밀번호가 포함된 요청
     * @return JWT 액세스 토큰과 리프레시 토큰
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login(@RequestBody @Valid AdminLoginRequest request) {
        return adminLoginUseCase.execute(request);
    }

    /**
     * 관리자 로그아웃을 처리합니다.
     * Redis에서 리프레시 토큰을 삭제합니다.
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        adminLogoutUseCase.execute();
    }

    /**
     * 모든 사용자에게 새로운 FCM 토픽을 추가합니다.
     *
     * @param request 토픽 이름과 기본 구독 여부가 포함된 요청
     */
    @PostMapping("/topics/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewTopicToAllUsers(@RequestBody @Valid AddNewTopicRequest request) {
        addNewTopicForAllUsersUseCase.execute(request.topic(), request.defaultSubscribed());
    }

    /**
     * 새로운 음악을 생성합니다.
     *
     * @param request 음악 정보가 포함된 요청
     */
    @PostMapping("/musics")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMusic(@RequestBody @Valid CreateMusicRequest request) {
        createMusicUseCase.execute(request);
    }

    /**
     * 기존 음악 정보를 수정합니다.
     *
     * @param request 수정할 음악 ID와 정보가 포함된 요청
     */
    @PatchMapping("/musics")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMusic(@RequestBody @Valid UpdateMusicRequest request) {
        updateMusicUseCase.execute(request);
    }

    /**
     * 음악을 삭제합니다.
     *
     * @param id 삭제할 음악 ID
     */
    @DeleteMapping("/musics/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMusic(@PathVariable Long id) {
        deleteMusicUseCase.execute(id);
    }

    /**
     * 새로운 공지사항을 생성합니다.
     *
     * @param request 공지사항 정보가 포함된 요청
     */
    @PostMapping("/announcements")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAnnouncement(@RequestBody @Valid CreateAnnouncementRequest request) {
        createAnnouncementUseCase.execute(request);
    }

    /**
     * 기존 공지사항을 수정합니다.
     *
     * @param request 수정할 공지사항 ID와 정보가 포함된 요청
     */
    @PatchMapping("/announcements")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAnnouncement(@RequestBody @Valid UpdateAnnouncementRequest request) {
        updateAnnouncementUseCase.execute(request);
    }

    /**
     * 공지사항을 삭제합니다.
     *
     * @param id 삭제할 공지사항 ID
     */
    @DeleteMapping("/announcements/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnnouncement(@PathVariable Long id) {
        deleteAnnouncementUseCase.execute(id);
    }

    /**
     * 회원 탈퇴 사유 목록을 페이지네이션하여 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 회원 탈퇴 사유 요약 목록
     */
    @GetMapping("/withdrawal-reasons")
    @ResponseStatus(HttpStatus.OK)
    public Page<WithdrawalReasonSummaryResponse> getWithdrawalReasons(Pageable pageable) {
        return getWithdrawalReasonsUseCase.execute(pageable);
    }

    /**
     * 특정 회원 탈퇴 사유의 상세 정보를 조회합니다.
     *
     * @param id 조회할 탈퇴 사유 ID
     * @return 탈퇴 사유 상세 정보
     */
    @GetMapping("/withdrawal-reasons/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WithdrawalReasonDetailResponse getWithdrawalReasonDetail(@PathVariable Long id) {
        return getWithdrawalReasonDetailUseCase.execute(id);
    }

    /**
     * 회원 탈퇴 사유를 삭제합니다.
     *
     * @param id 삭제할 탈퇴 사유 ID
     */
    @DeleteMapping("/withdrawal-reasons/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWithdrawalReason(@PathVariable Long id) {
        deleteWithdrawalReasonUseCase.execute(id);
    }

    /**
     * 특정 디바이스 토큰들에게 알림을 전송합니다.
     *
     * @param request 수신자 이메일 목록과 알림 내용이 포함된 요청
     */
    @PostMapping("/notifications/send")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendNotification(@RequestBody @Valid NotificationSendRequest request) {
        sendToDeviceTokensUseCase.execute(request);
    }

    /**
     * 특정 FCM 토픽을 구독한 사용자들에게 알림을 전송합니다.
     *
     * @param request 토픽 이름과 알림 내용이 포함된 요청
     */
    @PostMapping("/notifications/topic/send")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendToTopic(@RequestBody @Valid NotificationTopicSendRequest request) {
        sendToTopicUseCase.execute(request);
    }

    /**
     * 모든 사용자에게 알림을 브로드캐스트합니다.
     *
     * @param request 알림 내용이 포함된 요청
     */
    @PostMapping("/notifications/broadcast")
    @ResponseStatus(HttpStatus.CREATED)
    public void broadcastToAllUsers(@RequestBody @Valid NotificationTopicSendRequest request) {
        broadCastToAllUsersUseCase.execute(request);
    }
}
