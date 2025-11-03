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

@RestController
@RequiredArgsConstructor
@RequestMapping("/focus-sessions")
public class FocusSessionController implements FocusSessionApiDocument {

    private final SaveFocusSessionUseCase saveFocusSessionUseCase;
    private final GetFocusSessionListUseCase getFocusSessionListUseCase;
    private final GetFocusSessionDetailUseCase getFocusSessionDetailUseCase;
    private final DeleteFocusSessionUseCase deleteFocusSessionUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FocusSessionResponse saveFocusSession(@Valid @RequestBody SaveFocusSessionRequest request) {
        return saveFocusSessionUseCase.execute(request);
    }

    @GetMapping
    public Page<FocusSessionListResponse> getFocusSessionList(Pageable pageable) {
        return getFocusSessionListUseCase.execute(pageable);
    }

    @GetMapping("/{focusSessionId}")
    public FocusSessionDetailResponse getFocusSessionDetail(@PathVariable Long focusSessionId) {
        return getFocusSessionDetailUseCase.execute(focusSessionId);
    }

    @DeleteMapping("/{focusSessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFocusSession(@PathVariable Long focusSessionId) {
        deleteFocusSessionUseCase.execute(focusSessionId);
    }
}
