package whispy_server.whispy.domain.statistics.activity.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklyActivityResponse;
import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklySessionExistsResponse;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.in.GetWeeklyActivityUseCase;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.in.GetWeeklySessionExistsUseCase;
import whispy_server.whispy.global.document.api.statistics.ActivityStatisticsApiDocument;

/**
 * 활동 통계 REST 컨트롤러.
 *
 * 주간 활동 통계 및 세션 존재 여부를 조회하는 인바운드 어댑터입니다.
 */
@RestController
@RequestMapping("/statistics/activity")
@RequiredArgsConstructor
public class ActivityStatisticsController implements ActivityStatisticsApiDocument {

    private final GetWeeklySessionExistsUseCase getWeeklySessionExistsUseCase;
    private final GetWeeklyActivityUseCase getWeeklyActivityUseCase;

    /**
     * 주간 세션 존재 여부를 조회합니다.
     *
     * @return 주간 세션 존재 여부 응답
     */
    @GetMapping("/weekly-exists")
    public WeeklySessionExistsResponse getWeeklySessionExists() {
        return getWeeklySessionExistsUseCase.execute();
    }

    /**
     * 주간 활동 통계를 조회합니다.
     *
     * @return 주간 활동 통계 응답
     */
    @GetMapping("/weekly")
    public WeeklyActivityResponse getWeeklyActivity() {
        return getWeeklyActivityUseCase.execute();
    }
}