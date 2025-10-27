package whispy_server.whispy.domain.sleepsession.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.request.SaveSleepSessionRequest;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionResponse;
import whispy_server.whispy.domain.sleepsession.application.port.in.SaveSleepSessionUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sleep-sessions")
public class SleepSessionController {

    private final SaveSleepSessionUseCase saveSleepSessionUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SleepSessionResponse saveSleepSession(@Valid @RequestBody SaveSleepSessionRequest request) {
        return saveSleepSessionUseCase.execute(request);
    }
}
