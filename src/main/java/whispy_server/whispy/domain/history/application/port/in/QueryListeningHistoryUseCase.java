package whispy_server.whispy.domain.history.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.history.adapter.in.web.dto.response.ListeningHistoryResponse;
import whispy_server.whispy.global.annotation.UseCase;

/**
 * 현재 사용자의 청취 기록을 조회하는 유스케이스이다.
 */
@UseCase
public interface QueryListeningHistoryUseCase {
    /**
     * 페이징 조건에 맞춰 청취 기록을 최신순으로 반환한다.
     *
     * @param pageable 페이지 번호 및 크기
     * @return 청취 기록 응답 페이지
     */
    Page<ListeningHistoryResponse> execute(Pageable pageable);
}
