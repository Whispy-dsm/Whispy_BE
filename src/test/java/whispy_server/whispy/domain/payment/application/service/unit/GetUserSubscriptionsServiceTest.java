package whispy_server.whispy.domain.payment.application.service.unit;
import whispy_server.whispy.domain.payment.application.service.GetUserSubscriptionsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.GetUserSubscriptionsResponse;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * GetUserSubscriptionsService의 단위 테스트 클래스
 *
 * 사용자 구독 정보 조회 서비스의 다양한 시나리오를 검증합니다.
 * 구독 정보 존재 여부에 따른 응답 반환을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetUserSubscriptionsService 테스트")
class GetUserSubscriptionsServiceTest {

    @InjectMocks
    private GetUserSubscriptionsService getUserSubscriptionsService;

    @Mock
    private QuerySubscriptionPort querySubscriptionPort;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PURCHASE_TOKEN = "test-token-12345";

    @Test
    @DisplayName("구독이 있는 경우 구독 정보를 반환한다")
    void whenSubscriptionExists_thenReturnsSubscription() {
        // given
        Subscription subscription = createSubscription();

        given(querySubscriptionPort.findByEmail(TEST_EMAIL))
                .willReturn(Optional.of(subscription));

        // when
        GetUserSubscriptionsResponse response = getUserSubscriptionsService.getUserSubscriptions(TEST_EMAIL);

        // then
        assertThat(response.subscriptions()).isPresent();
        assertThat(response.subscriptions().get()).isEqualTo(subscription);
        assertThat(response.subscriptions().get().email()).isEqualTo(TEST_EMAIL);
        assertThat(response.subscriptions().get().productType()).isEqualTo(ProductType.MONTHLY);
    }

    @Test
    @DisplayName("구독이 없는 경우 빈 Optional을 반환한다")
    void whenNoSubscriptionExists_thenReturnsEmptyOptional() {
        // given
        given(querySubscriptionPort.findByEmail(TEST_EMAIL))
                .willReturn(Optional.empty());

        // when
        GetUserSubscriptionsResponse response = getUserSubscriptionsService.getUserSubscriptions(TEST_EMAIL);

        // then
        assertThat(response.subscriptions()).isEmpty();
    }

    @Test
    @DisplayName("다양한 구독 상태의 정보를 조회할 수 있다")
    void whenDifferentSubscriptionStates_thenReturnsCorrectly() {
        // given
        Subscription canceledSubscription = createSubscriptionWithState(SubscriptionState.CANCELED);

        given(querySubscriptionPort.findByEmail(TEST_EMAIL))
                .willReturn(Optional.of(canceledSubscription));

        // when
        GetUserSubscriptionsResponse response = getUserSubscriptionsService.getUserSubscriptions(TEST_EMAIL);

        // then
        assertThat(response.subscriptions()).isPresent();
        assertThat(response.subscriptions().get().subscriptionState()).isEqualTo(SubscriptionState.CANCELED);
    }

    @Test
    @DisplayName("다양한 구독 상태의 구독 정보를 조회할 수 있다")
    void whenDifferentSubscriptionStatesAgain_thenReturnsCorrectly() {
        // given
        Subscription expiredSubscription = createSubscriptionWithState(SubscriptionState.EXPIRED);

        given(querySubscriptionPort.findByEmail(TEST_EMAIL))
                .willReturn(Optional.of(expiredSubscription));

        // when
        GetUserSubscriptionsResponse response = getUserSubscriptionsService.getUserSubscriptions(TEST_EMAIL);

        // then
        assertThat(response.subscriptions()).isPresent();
        assertThat(response.subscriptions().get().subscriptionState()).isEqualTo(SubscriptionState.EXPIRED);
    }

    /**
     * 테스트용 기본 Subscription 객체를 생성합니다.
     *
     * @return 생성된 Subscription 객체
     */
    private Subscription createSubscription() {
        return new Subscription(
                1L,
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                ProductType.MONTHLY,
                LocalDateTime.now().minusDays(15),
                SubscriptionState.ACTIVE,
                true,
                LocalDateTime.now().plusDays(15)
        );
    }

    /**
     * 테스트용 Subscription 객체를 특정 상태로 생성합니다.
     *
     * @param state 구독 상태
     * @return 생성된 Subscription 객체
     */
    private Subscription createSubscriptionWithState(SubscriptionState state) {
        return new Subscription(
                1L,
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                ProductType.MONTHLY,
                LocalDateTime.now().minusDays(15),
                state,
                true,
                LocalDateTime.now().plusDays(15)
        );
    }

}
