package whispy_server.whispy.domain.meditationsession.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.meditationsession.adapter.in.web.dto.response.MeditationSessionListResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 명상 세션 목록 조회 유스케이스 인터페이스.
 *
 * 현재 사용자의 명상 세션 목록을 페이지 단위로 조회하는 애플리케이션 작업을 정의합니다.
 */
@UseCase
public interface GetMeditationSessionListUseCase {

    /**
     * 현재 사용자의 명상 세션 목록을 페이지 단위로 조회합니다.
     *
     * @param pageable 페이지 정보 (페이지 번호, 크기, 정렬)
     * @return 명상 세션 목록 페이지
     */
    Page<MeditationSessionListResponse> execute(Pageable pageable);
}
