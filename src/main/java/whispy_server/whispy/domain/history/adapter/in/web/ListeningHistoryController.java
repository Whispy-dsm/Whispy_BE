package whispy_server.whispy.domain.history.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.history.adapter.in.web.dto.response.ListeningHistoryResponse;
import whispy_server.whispy.domain.history.application.port.in.QueryListeningHistoryUseCase;
import whispy_server.whispy.domain.history.application.port.in.RecordListeningUseCase;
import whispy_server.whispy.global.document.api.history.ListeningHistoryApiDocument;

/**
 * 청취 이력 기록/조회 API를 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/listening-history")
@RequiredArgsConstructor
public class ListeningHistoryController implements ListeningHistoryApiDocument {

    private final RecordListeningUseCase recordListeningUseCase;
    private final QueryListeningHistoryUseCase queryListeningHistoryUseCase;

    /**
     * 특정 음악 청취 이벤트를 기록한다.
     *
     * @param musicId 청취한 음악 ID
     */
    @PostMapping("/{musicId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void recordListening(@PathVariable Long musicId) {
        recordListeningUseCase.execute(musicId);
    }

    /**
     * 내 청취 이력을 페이지 단위로 조회한다.
     *
     * @param pageable 페이지 조건
     * @return 청취 이력 페이지
     */
    @GetMapping("/my")
    public Page<ListeningHistoryResponse> getMyHistory(Pageable pageable) {
        return queryListeningHistoryUseCase.execute(pageable);
    }
}
