package whispy_server.whispy.domain.history.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import whispy_server.whispy.domain.history.adapter.in.web.dto.request.RecordListeningRequest;
import whispy_server.whispy.domain.history.adapter.in.web.dto.response.ListeningHistoryResponse;
import whispy_server.whispy.domain.history.application.port.in.QueryListeningHistoryUseCase;
import whispy_server.whispy.domain.history.application.port.in.RecordListeningUseCase;
import whispy_server.whispy.global.document.api.history.ListeningHistoryApiDocument;

@RestController
@RequestMapping("/listening-history")
@RequiredArgsConstructor
public class ListeningHistoryController implements ListeningHistoryApiDocument {

    private final RecordListeningUseCase recordListeningUseCase;
    private final QueryListeningHistoryUseCase queryListeningHistoryUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void recordListening(@RequestBody RecordListeningRequest request) {
        recordListeningUseCase.execute(request.musicId());
    }

    @GetMapping
    public Page<ListeningHistoryResponse> getMyHistory(Pageable pageable) {
        return queryListeningHistoryUseCase.execute(pageable);
    }
}