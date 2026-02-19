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
import whispy_server.whispy.global.annotation.UserAction;
import whispy_server.whispy.global.utils.redis.StatisticsCacheVersionManager;

import java.time.LocalDateTime;

/**
 * 수면 세션 저장 서비스.
 *
 * 새로운 수면 세션을 저장하는 유스케이스 구현입니다.
 * 현재 인증된 사용자의 정보를 포함하여 세션을 저장합니다.
 */
@Service
@RequiredArgsConstructor
public class SaveSleepSessionService implements SaveSleepSessionUseCase {

    private final SleepSessionSavePort sleepSessionSavePort;
    private final UserFacadeUseCase userFacadeUseCase;
    private final StatisticsCacheVersionManager statisticsCacheVersionManager;

    /**
     * 새로운 수면 세션을 저장합니다.
     *
     * @param request 수면 세션 저장 요청
     * @return 저장된 세션 정보
     */
    @UserAction("수면 세션 저장")
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
        statisticsCacheVersionManager.bumpUserVersionAfterCommit(user.id());

        return SleepSessionResponse.from(saved);
    }
}
