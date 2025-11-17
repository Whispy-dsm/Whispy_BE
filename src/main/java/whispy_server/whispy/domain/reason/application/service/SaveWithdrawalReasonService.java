package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.request.SaveWithdrawalReasonRequest;
import whispy_server.whispy.domain.reason.application.port.in.SaveWithdrawalReasonUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonSavePort;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;
import whispy_server.whispy.global.exception.domain.reason.InvalidWithdrawalReasonDetailException;

@Service
@RequiredArgsConstructor
public class SaveWithdrawalReasonService implements SaveWithdrawalReasonUseCase {

    private final WithdrawalReasonSavePort withdrawalReasonSavePort;

    @Override
    @Transactional
    public void execute(SaveWithdrawalReasonRequest request) {
        validateDetailContent(request.reasonType(), request.detailContent());

        WithdrawalReason withdrawalReason = new WithdrawalReason(
                null,
                request.reasonType(),
                request.detailContent(),
                null
        );

        withdrawalReasonSavePort.save(withdrawalReason);
    }
    
    private void validateDetailContent(WithdrawalReasonType reasonType, String detailContent) {
        if (reasonType == WithdrawalReasonType.OTHER) {
            if (detailContent == null || detailContent.isBlank()) {
                throw InvalidWithdrawalReasonDetailException.EXCEPTION;
            }
        } else {
            if (detailContent != null && !detailContent.isBlank()) {
                throw InvalidWithdrawalReasonDetailException.EXCEPTION;
            }
        }
    }
}
