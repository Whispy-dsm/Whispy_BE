package whispy_server.whispy.domain.reason.model;

import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;
import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;

@Aggregate
public record WithdrawalReason(
        Long id,
        WithdrawalReasonType reasonType,
        String detailContent,
        LocalDateTime createdAt
) {
}
