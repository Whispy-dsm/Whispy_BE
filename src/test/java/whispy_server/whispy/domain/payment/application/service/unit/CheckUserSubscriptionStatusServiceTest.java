package whispy_server.whispy.domain.payment.application.service.unit;
import whispy_server.whispy.domain.payment.application.service.CheckUserSubscriptionStatusService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.CheckUserSubscriptionStatusResponse;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * CheckUserSubscriptionStatusService의 단위 테스트 클래스
 *
 * 사용자 구독 상태 확인 서비스의 다양한 시나리오를 검증합니다.
 * 구독 상태별 활성 여부 판단 로직을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CheckUserSubscriptionStatusService 테스트")
class CheckUserSubscriptionStatusServiceTest {

    @InjectMocks
    private CheckUserSubscriptionStatusService checkUserSubscriptionStatusService;

    @Mock
    private QuerySubscriptionPort querySubscriptionPort;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PURCHASE_TOKEN = "test-token-12345";

    @Test
    @DisplayName("활성 구독이 있고 만료 전인 경우 true를 반환한다")
    void whenActiveSubscriptionExists_thenReturnsTrue() {
        // given
        Subscription activeSubscription = createSubscription(
                SubscriptionState.ACTIVE,
                LocalDateTime.now().plusDays(30)
        );

        given(querySubscriptionPort.findActiveSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(activeSubscription));

        // when
        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed(TEST_EMAIL);

        // then
        assertThat(response.isSubscribed()).isTrue();
    }

    @Test
    @DisplayName("활성 구독이 있지만 만료된 경우 false를 반환한다")
    void whenActiveSubscriptionExpired_thenReturnsFalse() {
        // given
        Subscription expiredSubscription = createSubscription(
                SubscriptionState.ACTIVE,
                LocalDateTime.now().minusDays(1)
        );

        given(querySubscriptionPort.findActiveSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(expiredSubscription));

        // when
        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed(TEST_EMAIL);

        // then
        assertThat(response.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("구독이 없는 경우 false를 반환한다")
    void whenNoSubscriptionExists_thenReturnsFalse() {
        // given
        given(querySubscriptionPort.findActiveSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.empty());

        // when
        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed(TEST_EMAIL);

        // then
        assertThat(response.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("CANCELED 상태이지만 만료 전인 경우 true를 반환한다")
    void whenCanceledButNotExpired_thenReturnsTrue() {
        // given
        Subscription canceledSubscription = createSubscription(
                SubscriptionState.CANCELED,
                LocalDateTime.now().plusDays(10)
        );

        given(querySubscriptionPort.findActiveSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(canceledSubscription));

        // when
        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed(TEST_EMAIL);

        // then
        assertThat(response.isSubscribed()).isTrue();
    }

    @Test
    @DisplayName("GRACE_PERIOD 상태이고 만료 전인 경우 true를 반환한다")
    void whenGracePeriodAndNotExpired_thenReturnsTrue() {
        // given
        Subscription gracePeriodSubscription = createSubscription(
                SubscriptionState.GRACE_PERIOD,
                LocalDateTime.now().plusDays(3)
        );

        given(querySubscriptionPort.findActiveSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(gracePeriodSubscription));

        // when
        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed(TEST_EMAIL);

        // then
        assertThat(response.isSubscribed()).isTrue();
    }

    @Test
    @DisplayName("EXPIRED 상태인 경우 false를 반환한다")
    void whenExpiredState_thenReturnsFalse() {
        // given
        Subscription expiredSubscription = createSubscription(
                SubscriptionState.EXPIRED,
                LocalDateTime.now().minusDays(1)
        );

        given(querySubscriptionPort.findActiveSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(expiredSubscription));

        // when
        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed(TEST_EMAIL);

        // then
        assertThat(response.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("PENDING 상태인 경우 false를 반환한다")
    void whenPendingState_thenReturnsFalse() {
        // given
        Subscription pendingSubscription = createSubscription(
                SubscriptionState.PENDING,
                LocalDateTime.now().plusDays(30)
        );

        given(querySubscriptionPort.findActiveSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(pendingSubscription));

        // when
        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed(TEST_EMAIL);

        // then
        assertThat(response.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("ON_HOLD 상태인 경우 false를 반환한다")
    void whenOnHoldState_thenReturnsFalse() {
        // given
        Subscription onHoldSubscription = createSubscription(
                SubscriptionState.ON_HOLD,
                LocalDateTime.now().plusDays(30)
        );

        given(querySubscriptionPort.findActiveSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(onHoldSubscription));

        // when
        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed(TEST_EMAIL);

        // then
        assertThat(response.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("REVOKED 상태인 경우 false를 반환한다")
    void whenRevokedState_thenReturnsFalse() {
        // given
        Subscription revokedSubscription = createSubscription(
                SubscriptionState.REVOKED,
                LocalDateTime.now().plusDays(30)
        );

        given(querySubscriptionPort.findActiveSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(revokedSubscription));

        // when
        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed(TEST_EMAIL);

        // then
        assertThat(response.isSubscribed()).isFalse();
    }

    /**
     * 테스트용 Subscription 객체를 생성합니다.
     *
     * @param state 구독 상태
     * @param expiryTime 만료 시간
     * @return 생성된 Subscription 객체
     */
    private Subscription createSubscription(SubscriptionState state, LocalDateTime expiryTime) {
        return new Subscription(
                1L,
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                ProductType.MONTHLY,
                LocalDateTime.now().minusDays(15),
                state,
                true,
                expiryTime
        );
    }
}
