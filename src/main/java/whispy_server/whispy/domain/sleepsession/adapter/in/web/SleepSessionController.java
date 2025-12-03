package whispy_server.whispy.domain.sleepsession.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.request.SaveSleepSessionRequest;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionDetailResponse;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionListResponse;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionResponse;
import whispy_server.whispy.domain.sleepsession.application.port.in.DeleteSleepSessionUseCase;
import whispy_server.whispy.domain.sleepsession.application.port.in.GetSleepSessionDetailUseCase;
import whispy_server.whispy.domain.sleepsession.application.port.in.GetSleepSessionListUseCase;
import whispy_server.whispy.domain.sleepsession.application.port.in.SaveSleepSessionUseCase;
import whispy_server.whispy.global.document.api.sleepsession.SleepSessionApiDocument;

/**
 * 수면 세션 REST 컨트롤러.
 *
 * 수면 세션의 생성, 조회, 삭제 기능을 제공하는 인바운드 어댑터입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sleep-sessions")
public class SleepSessionController implements SleepSessionApiDocument {

    private final SaveSleepSessionUseCase saveSleepSessionUseCase;
    private final GetSleepSessionListUseCase getSleepSessionListUseCase;
    private final GetSleepSessionDetailUseCase getSleepSessionDetailUseCase;
    private final DeleteSleepSessionUseCase deleteSleepSessionUseCase;

    /**
     * 새로운 수면 세션을 저장합니다.
     *
     * @param request 수면 세션 저장 요청 (시작 시간, 종료 시간, 지속 시간)
     * @return 저장된 수면 세션 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SleepSessionResponse saveSleepSession(@Valid @RequestBody SaveSleepSessionRequest request) {
        return saveSleepSessionUseCase.execute(request);
    }

    /**
     * 현재 사용자의 수면 세션 목록을 페이지로 조회합니다.
     *
     * @param pageable 페이지 정보 (페이지 번호, 크기, 정렬)
     * @return 수면 세션 목록 페이지
     */
    @GetMapping
    public Page<SleepSessionListResponse> getSleepSessionList(Pageable pageable) {
        return getSleepSessionListUseCase.execute(pageable);
    }

    /**
     * 특정 수면 세션의 상세 정보를 조회합니다.
     *
     * @param sleepSessionId 조회할 수면 세션 ID
     * @return 수면 세션 상세 정보
     */
    @GetMapping("/{sleepSessionId}")
    public SleepSessionDetailResponse getSleepSessionDetail(@PathVariable Long sleepSessionId) {
        return getSleepSessionDetailUseCase.execute(sleepSessionId);
    }

    /**
     * 특정 수면 세션을 삭제합니다.
     *
     * @param sleepSessionId 삭제할 수면 세션 ID
     */
    @DeleteMapping("/{sleepSessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSleepSession(@PathVariable Long sleepSessionId) {
        deleteSleepSessionUseCase.execute(sleepSessionId);
    }
}
