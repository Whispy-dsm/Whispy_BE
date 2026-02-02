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
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 명상 세션 저장 서비스.
 *
 * 명상 세션을 저장하고 오늘의 명상 통계를 계산하는 유스케이스 구현입니다.
 */
@Service
@RequiredArgsConstructor
public class SaveMeditationSessionService implements SaveMeditationSessionUseCase {

    private final MeditationSessionSavePort meditationSessionSavePort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final QueryMeditationStatisticsPort queryMeditationStatisticsPort;

    /**
     * 명상 세션을 저장합니다.
     *
     * @param request 저장할 명상 세션의 요청 DTO
     * @return 저장된 명상 세션과 오늘의 총 명상 시간(분)을 포함한 응답 DTO
     */
    @UserAction("명상 세션 저장")
    @Transactional
    @Override
    public MeditationSessionResponse execute(SaveMeditationSessionRequest request) {
        User user = userFacadeUseCase.currentUser();

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

    /**
     * 오늘의 총 명상 시간을 계산합니다.
     *
     * 오늘 00:00:00부터 23:59:59까지의 명상 세션 총 시간을 집계합니다.
     * 방금 저장한 세션을 포함한 총 시간이 반환됩니다.
     *
     * @param userId 계산할 사용자 ID
     * @return 오늘의 총 명상 시간(분)
     */
    private int calculateTodayTotalMinutes(Long userId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

        return queryMeditationStatisticsPort.getTotalMinutes(userId, startOfDay, endOfDay);
    }
}
