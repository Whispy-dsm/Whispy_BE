package whispy_server.whispy.domain.reason.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.request.SaveWithdrawalReasonRequest;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonSavePort;
import whispy_server.whispy.domain.reason.application.service.SaveWithdrawalReasonService;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * SaveWithdrawalReasonService의 단위 테스트 클래스
 * <p>
 * 탈퇴 사유 저장 서비스의 다양한 시나리오를 검증합니다.
 * 탈퇴 사유 저장 로직을 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SaveWithdrawalReasonService 테스트")
class SaveWithdrawalReasonServiceTest {

    @InjectMocks
    private SaveWithdrawalReasonService saveWithdrawalReasonService;

    @Mock
    private WithdrawalReasonSavePort withdrawalReasonSavePort;

    @Test
    @DisplayName("탈퇴 사유를 저장할 수 있다")
    void whenValidReason_thenSavesSuccessfully() {
        // given
        SaveWithdrawalReasonRequest request = new SaveWithdrawalReasonRequest(WithdrawalReasonType.NOT_USEFUL, null);

        // when
        saveWithdrawalReasonService.execute(request);

        // then
        verify(withdrawalReasonSavePort).save(any(WithdrawalReason.class));
    }

    @Test
    @DisplayName("OTHER 타입일 때 상세 내용과 함께 저장할 수 있다")
    void whenOtherTypeWithDetail_thenSavesSuccessfully() {
        // given
        SaveWithdrawalReasonRequest request = new SaveWithdrawalReasonRequest(WithdrawalReasonType.OTHER, "서비스가 불편해서");

        // when
        saveWithdrawalReasonService.execute(request);

        // then
        verify(withdrawalReasonSavePort).save(any(WithdrawalReason.class));
    }

}
