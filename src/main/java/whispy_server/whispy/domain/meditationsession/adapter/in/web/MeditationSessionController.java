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

@RestController
@RequiredArgsConstructor
@RequestMapping("/meditation-sessions")
public class MeditationSessionController implements MeditationSessionApiDocument {

    private final SaveMeditationSessionUseCase saveMeditationSessionUseCase;
    private final GetMeditationSessionListUseCase getMeditationSessionListUseCase;
    private final GetMeditationSessionDetailUseCase getMeditationSessionDetailUseCase;
    private final DeleteMeditationSessionUseCase deleteMeditationSessionUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MeditationSessionResponse saveMeditationSession(@Valid @RequestBody SaveMeditationSessionRequest request) {
        return saveMeditationSessionUseCase.execute(request);
    }

    @GetMapping
    public Page<MeditationSessionListResponse> getMeditationSessionList(Pageable pageable) {
        return getMeditationSessionListUseCase.execute(pageable);
    }

    @GetMapping("/{meditationSessionId}")
    public MeditationSessionDetailResponse getMeditationSessionDetail(@PathVariable Long meditationSessionId) {
        return getMeditationSessionDetailUseCase.execute(meditationSessionId);
    }

    @DeleteMapping("/{meditationSessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeditationSession(@PathVariable Long meditationSessionId) {
        deleteMeditationSessionUseCase.execute(meditationSessionId);
    }
}
