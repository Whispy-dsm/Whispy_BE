package whispy_server.whispy.domain.reason.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonDetailResponse;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.domain.reason.application.service.GetWithdrawalReasonDetailService;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;
import whispy_server.whispy.global.exception.domain.reason.WithdrawalReasonNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

/**
 * GetWithdrawalReasonDetailService의 단위 테스트 클래스
 *
 * 탈퇴 사유 상세 조회 서비스의 다양한 시나리오를 검증합니다.
 * 탈퇴 사유 상세 조회 및 예외 처리 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetWithdrawalReasonDetailService 테스트")
class GetWithdrawalReasonDetailServiceTest {

    @InjectMocks
    private GetWithdrawalReasonDetailService getWithdrawalReasonDetailService;

    @Mock
    private WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    @Test
    @DisplayName("탈퇴 사유 상세 정보를 조회할 수 있다")
    void whenReasonExists_thenReturnsDetails() {
        // given
        Long reasonId = 1L;
        WithdrawalReason reason = new WithdrawalReason(
                reasonId,
                WithdrawalReasonType.NOT_USEFUL,
                "테스트 사유",
                LocalDateTime.now()
        );

        given(withdrawalReasonQueryPort.findById(reasonId)).willReturn(Optional.of(reason));

        // when
        WithdrawalReasonDetailResponse response = getWithdrawalReasonDetailService.execute(reasonId);

        // then
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 탈퇴 사유 조회 시 예외가 발생한다")
    void whenReasonNotFound_thenThrowsException() {
        // given
        Long reasonId = 999L;
        given(withdrawalReasonQueryPort.findById(reasonId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> getWithdrawalReasonDetailService.execute(reasonId))
                .isInstanceOf(WithdrawalReasonNotFoundException.class);
    }
}
