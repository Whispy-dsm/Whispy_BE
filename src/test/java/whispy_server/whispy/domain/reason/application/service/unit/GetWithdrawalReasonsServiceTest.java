package whispy_server.whispy.domain.reason.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonSummaryResponse;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.domain.reason.application.service.GetWithdrawalReasonsService;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * GetWithdrawalReasonsService의 단위 테스트 클래스
 *
 * 탈퇴 사유 목록 조회 서비스의 다양한 시나리오를 검증합니다.
 * 페이지네이션된 탈퇴 사유 목록 조회 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetWithdrawalReasonsService 테스트")
class GetWithdrawalReasonsServiceTest {

    @InjectMocks
    private GetWithdrawalReasonsService getWithdrawalReasonsService;

    @Mock
    private WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    @Test
    @DisplayName("탈퇴 사유 목록을 조회할 수 있다")
    void whenQueryingReasons_thenReturnsPagedResults() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<WithdrawalReason> reasons = List.of(
                createWithdrawalReason(1L, 1L),
                createWithdrawalReason(2L, 2L)
        );
        Page<WithdrawalReason> reasonPage = new PageImpl<>(reasons, pageable, reasons.size());

        given(withdrawalReasonQueryPort.findAll(pageable)).willReturn(reasonPage);

        // when
        Page<WithdrawalReasonSummaryResponse> result = getWithdrawalReasonsService.execute(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
    }

    private WithdrawalReason createWithdrawalReason(Long id, Long userId) {
        return new WithdrawalReason(id, WithdrawalReasonType.NOT_USEFUL, "테스트 사유", LocalDateTime.now());
    }

    @Test
    @DisplayName("탈퇴 사유가 없을 때 빈 페이지를 반환한다")
    void whenNoReasons_thenReturnsEmptyPage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<WithdrawalReason> emptyPage = Page.empty(pageable);

        given(withdrawalReasonQueryPort.findAll(pageable)).willReturn(emptyPage);

        // when
        Page<WithdrawalReasonSummaryResponse> result = getWithdrawalReasonsService.execute(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
    }
}
