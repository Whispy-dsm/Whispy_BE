package whispy_server.whispy.domain.sleepsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.request.SaveSleepSessionRequest;
import whispy_server.whispy.domain.sleepsession.adapter.in.web.dto.response.SleepSessionResponse;
import whispy_server.whispy.domain.sleepsession.application.port.in.SaveSleepSessionUseCase;
import whispy_server.whispy.domain.sleepsession.application.port.out.SleepSessionSavePort;
import whispy_server.whispy.domain.sleepsession.model.SleepSession;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SaveSleepSessionService implements SaveSleepSessionUseCase {

    private final SleepSessionSavePort sleepSessionSavePort;
    private final UserFacadeUseCase userFacadeUseCase;

    @Transactional
    @Override
    public SleepSessionResponse execute(SaveSleepSessionRequest request) {
        User user = userFacadeUseCase.currentUser();

        SleepSession sleepSession = new SleepSession(
                null,
                user.id(),
                request.startedAt(),
                request.endedAt(),
                request.durationSeconds(),
                LocalDateTime.now()
        );

        SleepSession saved = sleepSessionSavePort.save(sleepSession);

        return SleepSessionResponse.from(saved);
    }
}
