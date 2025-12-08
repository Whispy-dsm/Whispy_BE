package whispy_server.whispy.domain.meditationsession.adapter.in.web;

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
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.request.SaveMeditationSessionRequest;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionDetailResponse;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionListResponse;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionResponse;
import whispy_server.whispy.domain.meditationsession.application.port.in.DeleteMeditationSessionUseCase;
import whispy_server.whispy.domain.meditationsession.application.port.in.GetMeditationSessionDetailUseCase;
import whispy_server.whispy.domain.meditationsession.application.port.in.GetMeditationSessionListUseCase;
import whispy_server.whispy.domain.meditationsession.application.port.in.SaveMeditationSessionUseCase;
import whispy_server.whispy.global.document.api.meditationsession.MeditationSessionApiDocument;

/**
 * 명상 세션 REST 컨트롤러.
 *
 * 명상 세션의 생성, 조회, 삭제 기능을 제공하는 인바운드 어댑터입니다.
 * 호흡 모드를 통해 다양한 명상 기법(빠른 호흡, 4-7-8 호흡 등)을 추적합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/meditation-sessions")
public class MeditationSessionController implements MeditationSessionApiDocument {

    private final SaveMeditationSessionUseCase saveMeditationSessionUseCase;
    private final GetMeditationSessionListUseCase getMeditationSessionListUseCase;
    private final GetMeditationSessionDetailUseCase getMeditationSessionDetailUseCase;
    private final DeleteMeditationSessionUseCase deleteMeditationSessionUseCase;

    /**
     * 새로운 명상 세션을 저장합니다.
     *
     * @param request 명상 세션 저장 요청 (시작 시간, 종료 시간, 지속 시간, 호흡 모드)
     * @return 저장된 명상 세션 정보 및 오늘의 총 명상 시간
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MeditationSessionResponse saveMeditationSession(@Valid @RequestBody SaveMeditationSessionRequest request) {
        return saveMeditationSessionUseCase.execute(request);
    }

    /**
     * 현재 사용자의 명상 세션 목록을 페이지로 조회합니다.
     *
     * @param pageable 페이지 정보 (페이지 번호, 크기, 정렬)
     * @return 명상 세션 목록 페이지
     */
    @GetMapping
    public Page<MeditationSessionListResponse> getMeditationSessionList(Pageable pageable) {
        return getMeditationSessionListUseCase.execute(pageable);
    }

    /**
     * 특정 명상 세션의 상세 정보를 조회합니다.
     *
     * @param meditationSessionId 조회할 명상 세션 ID
     * @return 명상 세션 상세 정보
     */
    @GetMapping("/{meditationSessionId}")
    public MeditationSessionDetailResponse getMeditationSessionDetail(@PathVariable Long meditationSessionId) {
        return getMeditationSessionDetailUseCase.execute(meditationSessionId);
    }

    /**
     * 특정 명상 세션을 삭제합니다.
     *
     * @param meditationSessionId 삭제할 명상 세션 ID
     */
    @DeleteMapping("/{meditationSessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeditationSession(@PathVariable Long meditationSessionId) {
        deleteMeditationSessionUseCase.execute(meditationSessionId);
    }
}
