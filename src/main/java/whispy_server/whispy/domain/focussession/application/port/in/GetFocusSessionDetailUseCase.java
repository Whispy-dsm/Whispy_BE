package whispy_server.whispy.domain.focussession.application.port.in;

import whispy_server.whispy.domain.focussession.adapter.in.web.dto.response.FocusSessionDetailResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 집중 세션 상세 조회 유스케이스 인터페이스.
 *
 * 특정 집중 세션의 상세 정보를 조회하는 애플리케이션 작업을 정의합니다.
 */
@UseCase
public interface GetFocusSessionDetailUseCase {

    /**
     * 특정 집중 세션의 상세 정보를 조회합니다.
     *
     * @param focusSessionId 조회할 집중 세션 ID
     * @return 집중 세션 상세 정보
     * @throws FocusSessionNotFoundException 해당 ID의 세션이 존재하지 않을 경우
     */
    FocusSessionDetailResponse execute(Long focusSessionId);
}
