package whispy_server.whispy.domain.reason.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.request.SaveWithdrawalReasonRequest;
import whispy_server.whispy.domain.reason.application.port.in.SaveWithdrawalReasonUseCase;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonSavePort;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;
import whispy_server.whispy.global.annotation.UserAction;
import whispy_server.whispy.global.exception.domain.reason.InvalidWithdrawalReasonDetailException;

/**
 * 탈퇴 사유 저장 UseCase 구현체.
 */
@Service
@RequiredArgsConstructor
public class SaveWithdrawalReasonService implements SaveWithdrawalReasonUseCase {

    private final WithdrawalReasonSavePort withdrawalReasonSavePort;

    /**
     * 탈퇴 사유 저장 요청을 처리한다.
     */
    @UserAction("탈퇴 사유 저장")
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
    
    /**
     * 상세 사유 입력 필요 여부를 검증합니다.
     *
     * 검증 규칙:
     * 1. reasonType == OTHER:
     *    - detailContent는 필수 (null이거나 비어있으면 예외)
     * 2. reasonType != OTHER:
     *    - detailContent는 비어있어야 함 (입력되면 예외)
     *
     * 예시:
     * - OTHER + "서비스가 만족스럽지 않아요" → 통과
     * - OTHER + null → 예외 (상세 사유 필수)
     * - LACK_OF_FEATURES + "기능이 부족해요" → 예외 (상세 사유 불필요)
     * - LACK_OF_FEATURES + null → 통과
     *
     * @param reasonType    탈퇴 사유 타입
     * @param detailContent 상세 사유
     * @throws InvalidWithdrawalReasonDetailException 상세 사유 규칙을 위반한 경우
     */
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
