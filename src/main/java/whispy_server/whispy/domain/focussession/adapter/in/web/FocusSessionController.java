package whispy_server.whispy.domain.focussession.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.request.SaveFocusSessionRequest;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionResponse;
import whispy_server.whispy.domain.focussession.application.port.in.SaveFocusSessionUseCase;
import whispy_server.whispy.global.document.api.focussession.FocusSessionApiDocument;

@RestController
@RequiredArgsConstructor
@RequestMapping("/focus-sessions")
public class FocusSessionController implements FocusSessionApiDocument {

    private final SaveFocusSessionUseCase saveFocusSessionUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FocusSessionResponse saveFocusSession(@Valid @RequestBody SaveFocusSessionRequest request) {
        return saveFocusSessionUseCase.execute(request);
    }
}
