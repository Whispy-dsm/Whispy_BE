package whispy_server.whispy.domain.meditationsession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.request.SaveMeditationSessionRequest;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionResponse;
import whispy_server.whispy.domain.meditationsession.application.port.in.SaveMeditationSessionUseCase;
import whispy_server.whispy.domain.meditationsession.application.port.out.MeditationSessionSavePort;
import whispy_server.whispy.domain.meditationsession.model.MeditationSession;
import whispy_server.whispy.domain.statistics.meditation.daily.application.port.out.QueryMeditationStatisticsPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.application.port.out.QueryUserPort;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.exception.domain.user.UserNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SaveMeditationSessionService implements SaveMeditationSessionUseCase {

    private final MeditationSessionSavePort meditationSessionSavePort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final QueryUserPort queryUserPort;
    private final QueryMeditationStatisticsPort queryMeditationStatisticsPort;

    @Transactional
    @Override
    public MeditationSessionResponse execute(SaveMeditationSessionRequest request) {
        String email = userFacadeUseCase.currentUser().email();
        User user = queryUserPort.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        MeditationSession meditationSession = new MeditationSession(
                null,
                user.id(),
                request.startedAt(),
                request.endedAt(),
                request.durationSeconds(),
                request.breatheMode(),
                LocalDateTime.now()
        );

        MeditationSession saved = meditationSessionSavePort.save(meditationSession);

        int todayTotalMinutes = calculateTodayTotalMinutes(user.id());

        return MeditationSessionResponse.from(saved, todayTotalMinutes);
    }

    private int calculateTodayTotalMinutes(Long userId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

        return queryMeditationStatisticsPort.getTotalMinutes(userId, startOfDay, endOfDay);
    }
}
