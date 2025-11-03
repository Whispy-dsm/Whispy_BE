package whispy_server.whispy.domain.sleepsession.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import whispy_server.whispy.domain.sleepsession.application.port.in.GetSleepSessionDetailUseCase;
import whispy_server.whispy.domain.sleepsession.application.port.in.GetSleepSessionListUseCase;
import whispy_server.whispy.domain.sleepsession.application.port.in.SaveSleepSessionUseCase;
import whispy_server.whispy.global.document.api.sleepsession.SleepSessionApiDocument;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sleep-sessions")
public class SleepSessionController implements SleepSessionApiDocument {

    private final SaveSleepSessionUseCase saveSleepSessionUseCase;
    private final GetSleepSessionListUseCase getSleepSessionListUseCase;
    private final GetSleepSessionDetailUseCase getSleepSessionDetailUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SleepSessionResponse saveSleepSession(@Valid @RequestBody SaveSleepSessionRequest request) {
        return saveSleepSessionUseCase.execute(request);
    }

    @GetMapping
    public Page<SleepSessionListResponse> getSleepSessionList(Pageable pageable) {
        return getSleepSessionListUseCase.execute(pageable);
    }

    @GetMapping("/{sleepSessionId}")
    public SleepSessionDetailResponse getSleepSessionDetail(@PathVariable Long sleepSessionId) {
        return getSleepSessionDetailUseCase.execute(sleepSessionId);
    }
}
