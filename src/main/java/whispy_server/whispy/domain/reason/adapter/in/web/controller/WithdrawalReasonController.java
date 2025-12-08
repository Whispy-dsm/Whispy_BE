package whispy_server.whispy.domain.reason.adapter.in.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.request.SaveWithdrawalReasonRequest;
import whispy_server.whispy.domain.reason.application.port.in.SaveWithdrawalReasonUseCase;
import whispy_server.whispy.global.document.api.reason.WithdrawalReasonApiDocument;

/**
 * 탈퇴 사유 등록 API 컨트롤러.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/withdrawal-reasons")
public class WithdrawalReasonController implements WithdrawalReasonApiDocument {

    private final SaveWithdrawalReasonUseCase saveWithdrawalReasonUseCase;

    /**
     * 탈퇴 사유를 저장한다.
     *
     * @param request 저장 요청
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveWithdrawalReason(@Valid @RequestBody SaveWithdrawalReasonRequest request) {
        saveWithdrawalReasonUseCase.execute(request);
    }
}
