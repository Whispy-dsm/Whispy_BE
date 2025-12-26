package whispy_server.whispy.domain.payment.application.service.unit;
import whispy_server.whispy.domain.payment.application.service.PurchaseProcessingService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.ValidatePurchaseResponse;
import whispy_server.whispy.domain.payment.application.port.out.GooglePlayApiPort;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.port.out.SubscriptionSavePort;
import whispy_server.whispy.domain.payment.application.service.domain.SubscriptionFactory;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.global.exception.domain.payment.SubscriptionAcknowledgmentFailedException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * PurchaseProcessingService의 단위 테스트 클래스
 *
 * 구매 처리 서비스의 다양한 시나리오를 검증합니다.
 * 구독 생성, 중복 구매 처리, acknowledgment 실패 처리를 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PurchaseProcessingService 테스트")
class PurchaseProcessingServiceTest {

    @InjectMocks
    private PurchaseProcessingService purchaseProcessingService;

    @Mock
    private SubscriptionSavePort subscriptionSavePort;

    @Mock
    private QuerySubscriptionPort querySubscriptionPort;

    @Mock
    private SubscriptionFactory subscriptionFactory;

    @Mock
    private GooglePlayApiPort googlePlayApiPort;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PURCHASE_TOKEN = "test-token-12345";
    private static final String TEST_SUBSCRIPTION_ID = "monthly_subscription";

    @Test
    @DisplayName("새로운 구매인 경우 구독을 생성하고 acknowledgment를 수행한다")
    void whenNewPurchase_thenCreatesSubscriptionAndAcknowledges() {
        // given
        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo();
        Subscription newSubscription = createSubscription();

        given(querySubscriptionPort.findByPurchaseToken(TEST_PURCHASE_TOKEN))
                .willReturn(Optional.empty());
        given(subscriptionFactory.createNewSubscription(
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                TEST_SUBSCRIPTION_ID,
                subscriptionInfo
        )).willReturn(newSubscription);
        doNothing().when(googlePlayApiPort).acknowledgeSubscription(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN);

        // when
        ValidatePurchaseResponse response = purchaseProcessingService.processValidatedPurchase(
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                TEST_SUBSCRIPTION_ID,
                subscriptionInfo
        );

        // then
        assertThat(response.isValid()).isTrue();
        verify(subscriptionSavePort).save(newSubscription);
        verify(googlePlayApiPort).acknowledgeSubscription(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN);
    }

    @Test
    @DisplayName("이미 처리된 구매인 경우 유효한 응답을 반환한다")
    void whenPurchaseAlreadyProcessed_thenReturnsValidResponse() {
        // given
        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo();
        Subscription existingSubscription = createSubscription();

        given(querySubscriptionPort.findByPurchaseToken(TEST_PURCHASE_TOKEN))
                .willReturn(Optional.of(existingSubscription));

        // when
        ValidatePurchaseResponse response = purchaseProcessingService.processValidatedPurchase(
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                TEST_SUBSCRIPTION_ID,
                subscriptionInfo
        );

        // then
        assertThat(response.isValid()).isTrue();
        verify(subscriptionSavePort, never()).save(any());
        verify(googlePlayApiPort, never()).acknowledgeSubscription(anyString(), anyString());
    }

    @Test
    @DisplayName("acknowledgment 실패 시 예외가 발생하지만 구독은 저장된다")
    void whenAcknowledgmentFails_thenThrowsExceptionButSubscriptionIsSaved() {
        // given
        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo();
        Subscription newSubscription = createSubscription();

        given(querySubscriptionPort.findByPurchaseToken(TEST_PURCHASE_TOKEN))
                .willReturn(Optional.empty());
        given(subscriptionFactory.createNewSubscription(
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                TEST_SUBSCRIPTION_ID,
                subscriptionInfo
        )).willReturn(newSubscription);
        doThrow(new RuntimeException("Acknowledgment failed"))
                .when(googlePlayApiPort).acknowledgeSubscription(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN);

        // when & then
        assertThatThrownBy(() -> purchaseProcessingService.processValidatedPurchase(
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                TEST_SUBSCRIPTION_ID,
                subscriptionInfo
        )).isInstanceOf(SubscriptionAcknowledgmentFailedException.class);

        // 구독은 이미 저장되었어야 함
        verify(subscriptionSavePort).save(newSubscription);
    }

    @Test
    @DisplayName("구독 생성 시 SubscriptionFactory를 호출한다")
    void whenCreatingSubscription_thenCallsSubscriptionFactory() {
        // given
        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo();
        Subscription newSubscription = createSubscription();

        given(querySubscriptionPort.findByPurchaseToken(TEST_PURCHASE_TOKEN))
                .willReturn(Optional.empty());
        given(subscriptionFactory.createNewSubscription(
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                TEST_SUBSCRIPTION_ID,
                subscriptionInfo
        )).willReturn(newSubscription);
        doNothing().when(googlePlayApiPort).acknowledgeSubscription(anyString(), anyString());

        // when
        purchaseProcessingService.processValidatedPurchase(
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                TEST_SUBSCRIPTION_ID,
                subscriptionInfo
        );

        // then
        verify(subscriptionFactory).createNewSubscription(
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                TEST_SUBSCRIPTION_ID,
                subscriptionInfo
        );
    }

    @Test
    @DisplayName("구매 처리 전에 중복 확인을 수행한다")
    void whenProcessingPurchase_thenChecksDuplicate() {
        // given
        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo();
        Subscription newSubscription = createSubscription();

        given(querySubscriptionPort.findByPurchaseToken(TEST_PURCHASE_TOKEN))
                .willReturn(Optional.empty());
        given(subscriptionFactory.createNewSubscription(anyString(), anyString(), anyString(), any()))
                .willReturn(newSubscription);
        doNothing().when(googlePlayApiPort).acknowledgeSubscription(anyString(), anyString());

        // when
        purchaseProcessingService.processValidatedPurchase(
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                TEST_SUBSCRIPTION_ID,
                subscriptionInfo
        );

        // then
        verify(querySubscriptionPort).findByPurchaseToken(TEST_PURCHASE_TOKEN);
    }

    /**
     * 테스트용 GooglePlaySubscriptionInfo 객체를 생성합니다.
     *
     * @return 생성된 GooglePlaySubscriptionInfo 객체
     */
    private GooglePlaySubscriptionInfo createSubscriptionInfo() {
        long currentTime = System.currentTimeMillis();
        return new GooglePlaySubscriptionInfo(
                TEST_PURCHASE_TOKEN,
                null,
                1,
                currentTime + (30L * 24 * 60 * 60 * 1000), // 30일 후
                currentTime,
                null
        );
    }

    /**
     * 테스트용 Subscription 객체를 생성합니다.
     *
     * @return 생성된 Subscription 객체
     */
    private Subscription createSubscription() {
        return new Subscription(
                1L,
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                ProductType.MONTHLY,
                LocalDateTime.now(),
                SubscriptionState.ACTIVE,
                true,
                LocalDateTime.now().plusDays(30)
        );
    }
}
