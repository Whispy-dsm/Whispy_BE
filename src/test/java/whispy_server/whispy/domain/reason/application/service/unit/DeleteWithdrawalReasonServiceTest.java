package whispy_server.whispy.domain.reason.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonDeletePort;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.domain.reason.application.service.DeleteWithdrawalReasonService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * DeleteWithdrawalReasonService의 단위 테스트 클래스
 *
 * 탈퇴 사유 삭제 서비스의 다양한 시나리오를 검증합니다.
 * 탈퇴 사유 삭제 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteWithdrawalReasonService 테스트")
class DeleteWithdrawalReasonServiceTest {

    @InjectMocks
    private DeleteWithdrawalReasonService deleteWithdrawalReasonService;

    @Mock
    private WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    @Mock
    private WithdrawalReasonDeletePort withdrawalReasonDeletePort;

    @Test
    @DisplayName("탈퇴 사유를 삭제할 수 있다")
    void whenValidId_thenDeletesSuccessfully() {
        // given
        Long reasonId = 1L;
        given(withdrawalReasonQueryPort.existsById(reasonId)).willReturn(true);

        // when
        deleteWithdrawalReasonService.execute(reasonId);

        // then
        verify(withdrawalReasonQueryPort).existsById(reasonId);
        verify(withdrawalReasonDeletePort).deleteById(reasonId);
    }
}
