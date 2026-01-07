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
import whispy_server.whispy.domain.reason.adapter.in.web.dto.response.WithdrawalReasonsByDateResponse;
import whispy_server.whispy.domain.reason.application.port.out.WithdrawalReasonQueryPort;
import whispy_server.whispy.domain.reason.application.service.GetWithdrawalReasonsByDateService;
import whispy_server.whispy.domain.reason.model.WithdrawalReason;
import whispy_server.whispy.domain.reason.model.types.WithdrawalReasonType;
import whispy_server.whispy.global.exception.domain.statistics.InvalidStatisticsDateException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

/**
 * GetWithdrawalReasonsByDateService의 단위 테스트 클래스
 *
 * 날짜별 탈퇴 이유 목록 조회 서비스의 다양한 시나리오를 검증합니다.
 * 특정 날짜의 탈퇴 이유 조회 및 날짜 검증 로직을 테스트합니다.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetWithdrawalReasonsByDateService 테스트")
class GetWithdrawalReasonsByDateServiceTest {

    @InjectMocks
    private GetWithdrawalReasonsByDateService service;

    @Mock
    private WithdrawalReasonQueryPort withdrawalReasonQueryPort;

    /**
     * 테스트용 WithdrawalReason 객체를 생성합니다.
     *
     * @param id 탈퇴 이유 ID
     * @param createdAt 생성 일시
     * @return 생성된 WithdrawalReason 객체
     */
    private WithdrawalReason createWithdrawalReason(Long id, LocalDateTime createdAt) {
        return new WithdrawalReason(
                id,
                WithdrawalReasonType.NOT_USEFUL,
                "테스트 탈퇴 사유",
                createdAt
        );
    }

    @Test
    @DisplayName("특정 날짜의 탈퇴 이유를 조회할 수 있다")
    void whenQueryingReasonsByDate_thenReturnsPagedResults() {
        // given
        LocalDate date = LocalDate.of(2024, 1, 15);
        Pageable pageable = PageRequest.of(0, 10);

        List<WithdrawalReason> reasons = List.of(
                createWithdrawalReason(1L, date.atTime(10, 30)),
                createWithdrawalReason(2L, date.atTime(14, 20))
        );
        Page<WithdrawalReason> reasonPage = new PageImpl<>(reasons, pageable, reasons.size());

        given(withdrawalReasonQueryPort.findAllByDate(date, pageable)).willReturn(reasonPage);

        // when
        Page<WithdrawalReasonsByDateResponse> result = service.execute(date, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).id()).isEqualTo(1L);
        assertThat(result.getContent().get(1).id()).isEqualTo(2L);
    }

    @Test
    @DisplayName("특정 날짜에 탈퇴 이유가 없으면 빈 페이지를 반환한다")
    void whenNoReasonsOnDate_thenReturnsEmptyPage() {
        // given
        LocalDate date = LocalDate.of(2024, 1, 15);
        Pageable pageable = PageRequest.of(0, 10);
        Page<WithdrawalReason> emptyPage = Page.empty(pageable);

        given(withdrawalReasonQueryPort.findAllByDate(date, pageable)).willReturn(emptyPage);

        // when
        Page<WithdrawalReasonsByDateResponse> result = service.execute(date, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("미래 날짜를 조회하면 예외가 발생한다")
    void whenQueryingFutureDate_thenThrowsException() {
        // given
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Pageable pageable = PageRequest.of(0, 10);

        // when & then
        assertThatThrownBy(() -> service.execute(futureDate, pageable))
                .isInstanceOf(InvalidStatisticsDateException.class);
    }

    @Test
    @DisplayName("오늘 날짜를 조회할 수 있다")
    void whenQueryingToday_thenReturnsResults() {
        // given
        LocalDate today = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10);

        List<WithdrawalReason> reasons = List.of(
                createWithdrawalReason(1L, today.atTime(10, 0))
        );
        Page<WithdrawalReason> reasonPage = new PageImpl<>(reasons, pageable, reasons.size());

        given(withdrawalReasonQueryPort.findAllByDate(today, pageable)).willReturn(reasonPage);

        // when
        Page<WithdrawalReasonsByDateResponse> result = service.execute(today, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("과거 날짜를 조회할 수 있다")
    void whenQueryingPastDate_thenReturnsResults() {
        // given
        LocalDate pastDate = LocalDate.of(2023, 12, 1);
        Pageable pageable = PageRequest.of(0, 10);

        List<WithdrawalReason> reasons = List.of(
                createWithdrawalReason(1L, pastDate.atTime(15, 30)),
                createWithdrawalReason(2L, pastDate.atTime(16, 45)),
                createWithdrawalReason(3L, pastDate.atTime(20, 10))
        );
        Page<WithdrawalReason> reasonPage = new PageImpl<>(reasons, pageable, reasons.size());

        given(withdrawalReasonQueryPort.findAllByDate(pastDate, pageable)).willReturn(reasonPage);

        // when
        Page<WithdrawalReasonsByDateResponse> result = service.execute(pastDate, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);
    }
}
