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
 * 날짜 범위별 탈퇴 이유 목록 조회 서비스의 다양한 시나리오를 검증합니다.
 * 날짜 범위의 탈퇴 이유 조회 및 날짜 범위 검증 로직을 테스트합니다.
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
    @DisplayName("날짜 범위 내의 탈퇴 이유를 조회할 수 있다")
    void whenQueryingReasonsByDateRange_thenReturnsPagedResults() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        Pageable pageable = PageRequest.of(0, 10);

        List<WithdrawalReason> reasons = List.of(
                createWithdrawalReason(1L, LocalDate.of(2024, 1, 5).atTime(10, 30)),
                createWithdrawalReason(2L, LocalDate.of(2024, 1, 15).atTime(14, 20)),
                createWithdrawalReason(3L, LocalDate.of(2024, 1, 25).atTime(16, 45))
        );
        Page<WithdrawalReason> reasonPage = new PageImpl<>(reasons, pageable, reasons.size());

        given(withdrawalReasonQueryPort.findAllByDateRange(startDate, endDate, pageable)).willReturn(reasonPage);

        // when
        Page<WithdrawalReasonsByDateResponse> result = service.execute(startDate, endDate, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent().get(0).id()).isEqualTo(1L);
        assertThat(result.getContent().get(1).id()).isEqualTo(2L);
        assertThat(result.getContent().get(2).id()).isEqualTo(3L);
    }

    @Test
    @DisplayName("날짜 범위 내에 탈퇴 이유가 없으면 빈 페이지를 반환한다")
    void whenNoReasonsInDateRange_thenReturnsEmptyPage() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        Pageable pageable = PageRequest.of(0, 10);
        Page<WithdrawalReason> emptyPage = Page.empty(pageable);

        given(withdrawalReasonQueryPort.findAllByDateRange(startDate, endDate, pageable)).willReturn(emptyPage);

        // when
        Page<WithdrawalReasonsByDateResponse> result = service.execute(startDate, endDate, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("종료 날짜가 미래이면 예외가 발생한다")
    void whenEndDateIsFuture_thenThrowsException() {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate futureEndDate = LocalDate.now().plusDays(1);
        Pageable pageable = PageRequest.of(0, 10);

        // when & then
        assertThatThrownBy(() -> service.execute(startDate, futureEndDate, pageable))
                .isInstanceOf(InvalidStatisticsDateException.class);
    }

    @Test
    @DisplayName("시작 날짜가 종료 날짜보다 이후이면 예외가 발생한다")
    void whenStartDateIsAfterEndDate_thenThrowsException() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 31);
        LocalDate endDate = LocalDate.of(2024, 1, 1);
        Pageable pageable = PageRequest.of(0, 10);

        // when & then
        assertThatThrownBy(() -> service.execute(startDate, endDate, pageable))
                .isInstanceOf(InvalidStatisticsDateException.class);
    }

    @Test
    @DisplayName("오늘을 포함한 날짜 범위를 조회할 수 있다")
    void whenQueryingRangeIncludingToday_thenReturnsResults() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10);

        List<WithdrawalReason> reasons = List.of(
                createWithdrawalReason(1L, LocalDate.now().atTime(10, 0))
        );
        Page<WithdrawalReason> reasonPage = new PageImpl<>(reasons, pageable, reasons.size());

        given(withdrawalReasonQueryPort.findAllByDateRange(startDate, endDate, pageable)).willReturn(reasonPage);

        // when
        Page<WithdrawalReasonsByDateResponse> result = service.execute(startDate, endDate, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("과거 날짜 범위를 조회할 수 있다")
    void whenQueryingPastDateRange_thenReturnsResults() {
        // given
        LocalDate startDate = LocalDate.of(2023, 12, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        Pageable pageable = PageRequest.of(0, 10);

        List<WithdrawalReason> reasons = List.of(
                createWithdrawalReason(1L, LocalDate.of(2023, 12, 5).atTime(15, 30)),
                createWithdrawalReason(2L, LocalDate.of(2023, 12, 15).atTime(16, 45)),
                createWithdrawalReason(3L, LocalDate.of(2023, 12, 25).atTime(20, 10))
        );
        Page<WithdrawalReason> reasonPage = new PageImpl<>(reasons, pageable, reasons.size());

        given(withdrawalReasonQueryPort.findAllByDateRange(startDate, endDate, pageable)).willReturn(reasonPage);

        // when
        Page<WithdrawalReasonsByDateResponse> result = service.execute(startDate, endDate, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(3);
    }

    @Test
    @DisplayName("같은 날짜로 범위를 조회할 수 있다")
    void whenQueryingSameDateRange_thenReturnsResults() {
        // given
        LocalDate date = LocalDate.of(2024, 1, 15);
        Pageable pageable = PageRequest.of(0, 10);

        List<WithdrawalReason> reasons = List.of(
                createWithdrawalReason(1L, date.atTime(10, 30)),
                createWithdrawalReason(2L, date.atTime(14, 20))
        );
        Page<WithdrawalReason> reasonPage = new PageImpl<>(reasons, pageable, reasons.size());

        given(withdrawalReasonQueryPort.findAllByDateRange(date, date, pageable)).willReturn(reasonPage);

        // when
        Page<WithdrawalReasonsByDateResponse> result = service.execute(date, date, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
    }
}
