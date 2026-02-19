package whispy_server.whispy.domain.focussession.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.request.SaveFocusSessionRequest;
import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionResponse;
import whispy_server.whispy.domain.focussession.application.port.in.SaveFocusSessionUseCase;
import whispy_server.whispy.domain.focussession.application.port.out.FocusSessionSavePort;
import whispy_server.whispy.domain.focussession.model.FocusSession;
import whispy_server.whispy.domain.statistics.focus.daily.application.port.out.QueryFocusStatisticsPort;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.global.annotation.UserAction;
import whispy_server.whispy.global.utils.redis.StatisticsCacheVersionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 집중 세션 저장 서비스.
 *
 * 새로운 집중 세션을 저장하는 유스케이스 구현입니다.
 * 현재 인증된 사용자의 정보를 포함하여 세션을 저장하고, 오늘의 총 집중 시간을 함께 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class SaveFocusSessionService implements SaveFocusSessionUseCase {

    private final FocusSessionSavePort focusSessionSavePort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final QueryFocusStatisticsPort queryFocusStatisticsPort;
    private final StatisticsCacheVersionManager statisticsCacheVersionManager;

    /**
     * 새로운 집중 세션을 저장합니다.
     *
     * @param request 집중 세션 저장 요청
     * @return 저장된 세션 정보 및 오늘의 총 집중 시간
     */
    @UserAction("집중 세션 저장")
    @Transactional
    @Override
    public FocusSessionResponse execute(SaveFocusSessionRequest request) {
        User user = userFacadeUseCase.currentUser();

        FocusSession focusSession = new FocusSession(
                null,
                user.id(),
                request.startedAt(),
                request.endedAt(),
                request.durationSeconds(),
                request.tag(),
                LocalDateTime.now()
        );

        FocusSession saved = focusSessionSavePort.save(focusSession);

        int todayTotalMinutes = calculateTodayTotalMinutes(user.id());
        statisticsCacheVersionManager.bumpUserVersionAfterCommit(user.id());

        return FocusSessionResponse.from(saved, todayTotalMinutes);
    }

    /**
     * 오늘의 총 집중 시간(분 단위)을 계산합니다.
     *
     * 오늘 00:00:00부터 23:59:59까지의 집중 세션 총 시간을 집계합니다.
     * 방금 저장한 세션을 포함한 총 시간이 반환됩니다.
     *
     * @param userId 사용자 ID
     * @return 오늘의 총 집중 시간(분 단위)
     */
    private int calculateTodayTotalMinutes(Long userId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

        return queryFocusStatisticsPort.getTotalMinutes(userId, startOfDay, endOfDay);
    }
}
