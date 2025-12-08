package whispy_server.whispy.domain.reason.model;

import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;
import whispy_server.whispy.global.annotation.Aggregate;

import java.time.LocalDateTime;

/**
 * 회원 탈퇴 사유 도메인 모델 (애그리게잇).
 *
 * 사용자의 회원 탈퇴 사유 정보를 담고 있는 도메인 모델입니다.
 *
 * @param id 탈퇴 사유 ID
 * @param reasonType 사유 타입
 * @param detailContent 상세 내용
 * @param createdAt 생성 일시
 */
@Aggregate
public record WithdrawalReason(
        Long id,
        WithdrawalReasonType reasonType,
        String detailContent,
        LocalDateTime createdAt
) {
}
