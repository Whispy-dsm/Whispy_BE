package whispy_server.whispy.domain.statistics.activity.applicatoin.port.in;

import whispy_server.whispy.domain.statistics.activity.adapter.in.web.dto.response.WeeklySessionExistsResponse;

public interface GetWeeklySessionExistsUseCase {
    WeeklySessionExistsResponse execute();
}
