package whispy_server.whispy.domain.focussession.adapter.in.web;

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
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.request.SaveFocusSessionRequest;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionDetailResponse;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionListResponse;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionResponse;
import whispy_server.whispy.domain.focussession.application.port.in.DeleteFocusSessionUseCase;
import whispy_server.whispy.domain.focussession.application.port.in.GetFocusSessionDetailUseCase;
import whispy_server.whispy.domain.focussession.application.port.in.GetFocusSessionListUseCase;
import whispy_server.whispy.domain.focussession.application.port.in.SaveFocusSessionUseCase;
import whispy_server.whispy.global.document.api.focussession.FocusSessionApiDocument;

/**
 * 집중 세션 REST 컨트롤러.
 *
 * 집중 세션의 생성, 조회, 삭제 기능을 제공하는 인바운드 어댑터입니다.
 * 태그를 통해 다양한 집중 활동(업무, 공부, 독서 등)을 추적합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/focus-sessions")
public class FocusSessionController implements FocusSessionApiDocument {

    private final SaveFocusSessionUseCase saveFocusSessionUseCase;
    private final GetFocusSessionListUseCase getFocusSessionListUseCase;
    private final GetFocusSessionDetailUseCase getFocusSessionDetailUseCase;
    private final DeleteFocusSessionUseCase deleteFocusSessionUseCase;

    /**
     * 새로운 집중 세션을 저장합니다.
     *
     * @param request 집중 세션 저장 요청 (시작 시간, 종료 시간, 지속 시간, 태그)
     * @return 저장된 집중 세션 정보 및 오늘의 총 집중 시간
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FocusSessionResponse saveFocusSession(@Valid @RequestBody SaveFocusSessionRequest request) {
        return saveFocusSessionUseCase.execute(request);
    }

    /**
     * 현재 사용자의 집중 세션 목록을 페이지로 조회합니다.
     *
     * @param pageable 페이지 정보 (페이지 번호, 크기, 정렬)
     * @return 집중 세션 목록 페이지
     */
    @GetMapping
    public Page<FocusSessionListResponse> getFocusSessionList(Pageable pageable) {
        return getFocusSessionListUseCase.execute(pageable);
    }

    /**
     * 특정 집중 세션의 상세 정보를 조회합니다.
     *
     * @param focusSessionId 조회할 집중 세션 ID
     * @return 집중 세션 상세 정보
     */
    @GetMapping("/{focusSessionId}")
    public FocusSessionDetailResponse getFocusSessionDetail(@PathVariable Long focusSessionId) {
        return getFocusSessionDetailUseCase.execute(focusSessionId);
    }

    /**
     * 특정 집중 세션을 삭제합니다.
     *
     * @param focusSessionId 삭제할 집중 세션 ID
     */
    @DeleteMapping("/{focusSessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFocusSession(@PathVariable Long focusSessionId) {
        deleteFocusSessionUseCase.execute(focusSessionId);
    }
}
