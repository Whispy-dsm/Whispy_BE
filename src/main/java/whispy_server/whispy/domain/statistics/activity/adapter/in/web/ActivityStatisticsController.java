package whispy_server.whispy.domain.statistics.activity.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklySessionExistsResponse;
import whispy_server.whispy.domain.statistics.activity.applicatoin.port.in.GetWeeklySessionExistsUseCase;
import whispy_server.whispy.global.document.api.statistics.ActivityStatisticsApiDocument;

@RestController
@RequestMapping("/statistics/activity")
@RequiredArgsConstructor
public class ActivityStatisticsController implements ActivityStatisticsApiDocument {

    private final GetWeeklySessionExistsUseCase getWeeklySessionExistsUseCase;

    @GetMapping("/weekly-exists")
    public WeeklySessionExistsResponse getWeeklySessionExists() {
        return getWeeklySessionExistsUseCase.execute();
    }
}
