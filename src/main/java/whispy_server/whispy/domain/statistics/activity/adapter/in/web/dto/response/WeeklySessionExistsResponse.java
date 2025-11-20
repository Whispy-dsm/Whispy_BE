package whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;
import java.util.Map;

@Schema(description = "주간 세션 존재 여부 응답")
public record WeeklySessionExistsResponse(
        @Schema(description = "요일별 세션 존재 여부")
        Map<DayOfWeek, Boolean> weeklyExists
) {
}
