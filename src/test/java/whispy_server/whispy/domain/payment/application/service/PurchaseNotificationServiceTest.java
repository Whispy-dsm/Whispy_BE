package whispy_server.whispy.domain.payment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.PubSubMessageDataRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.PubSubMessageRequest;
import whispy_server.whispy.domain.payment.application.port.out.GooglePlayApiPort;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.port.out.SubscriptionSavePort;
import whispy_server.whispy.domain.payment.application.service.domain.SubscriptionFactory;
import whispy_server.whispy.domain.payment.application.service.domain.SubscriptionUpdater;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.global.exception.domain.payment.InvalidSubscriptionNotificationException;
import whispy_server.whispy.global.exception.domain.payment.PurchaseNotificationProcessingFailedException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * PurchaseNotificationService의 단위 테스트 클래스
 * <p>
 * 구매 알림 처리 서비스의 다양한 시나리오를 검증합니다.
 * Google Play Pub/Sub 메시지 처리 및 구독 상태 업데이트를 테스트합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PurchaseNotificationService 테스트")
class PurchaseNotificationServiceTest {

    @InjectMocks
    private PurchaseNotificationService purchaseNotificationService;

    @Mock
    private SubscriptionSavePort subscriptionSavePort;

    @Mock
    private QuerySubscriptionPort querySubscriptionPort;

    @Mock
    private GooglePlayApiPort googlePlayApiPort;

    @Mock
    private SubscriptionFactory subscriptionFactory;

    @Mock
    private SubscriptionUpdater subscriptionUpdater;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_PURCHASE_TOKEN = "test-token-12345";
    private static final String TEST_SUBSCRIPTION_ID = "monthly_subscription";
    private static final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("RECOVERED 알림을 받으면 구독 상태를 ACTIVE로 업데이트한다")
    void whenRecoveredNotification_thenUpdatesStateToActive() {
        // given
        String jsonData = createNotificationJson(1, TEST_PURCHASE_TOKEN, TEST_SUBSCRIPTION_ID); // RECOVERED = 1
        PubSubMessageRequest pubSubMessage = createPubSubMessage(jsonData);

        doNothing().when(subscriptionUpdater).updateState(TEST_PURCHASE_TOKEN, SubscriptionState.ACTIVE);

        // when
        purchaseNotificationService.processPubSubMessage(pubSubMessage);

        // then
        verify(subscriptionUpdater).updateState(TEST_PURCHASE_TOKEN, SubscriptionState.ACTIVE);
    }

    @Test
    @DisplayName("CANCELED 알림을 받으면 구독 상태를 CANCELED로 업데이트한다")
    void whenCanceledNotification_thenUpdatesStateToCanceled() {
        // given
        String jsonData = createNotificationJson(3, TEST_PURCHASE_TOKEN, TEST_SUBSCRIPTION_ID); // CANCELED = 3
        PubSubMessageRequest pubSubMessage = createPubSubMessage(jsonData);

        doNothing().when(subscriptionUpdater).updateState(TEST_PURCHASE_TOKEN, SubscriptionState.CANCELED);

        // when
        purchaseNotificationService.processPubSubMessage(pubSubMessage);

        // then
        verify(subscriptionUpdater).updateState(TEST_PURCHASE_TOKEN, SubscriptionState.CANCELED);
    }

    @Test
    @DisplayName("ON_HOLD 알림을 받으면 구독 상태를 ON_HOLD로 업데이트한다")
    void whenOnHoldNotification_thenUpdatesStateToOnHold() {
        // given
        String jsonData = createNotificationJson(5, TEST_PURCHASE_TOKEN, TEST_SUBSCRIPTION_ID); // ON_HOLD = 5
        PubSubMessageRequest pubSubMessage = createPubSubMessage(jsonData);

        doNothing().when(subscriptionUpdater).updateState(TEST_PURCHASE_TOKEN, SubscriptionState.ON_HOLD);

        // when
        purchaseNotificationService.processPubSubMessage(pubSubMessage);

        // then
        verify(subscriptionUpdater).updateState(TEST_PURCHASE_TOKEN, SubscriptionState.ON_HOLD);
    }

    @Test
    @DisplayName("GRACE_PERIOD 알림을 받으면 구독 상태를 GRACE_PERIOD로 업데이트한다")
    void whenGracePeriodNotification_thenUpdatesStateToGracePeriod() {
        // given
        String jsonData = createNotificationJson(6, TEST_PURCHASE_TOKEN, TEST_SUBSCRIPTION_ID); // GRACE_PERIOD = 6
        PubSubMessageRequest pubSubMessage = createPubSubMessage(jsonData);

        doNothing().when(subscriptionUpdater).updateState(TEST_PURCHASE_TOKEN, SubscriptionState.GRACE_PERIOD);

        // when
        purchaseNotificationService.processPubSubMessage(pubSubMessage);

        // then
        verify(subscriptionUpdater).updateState(TEST_PURCHASE_TOKEN, SubscriptionState.GRACE_PERIOD);
    }

    @Test
    @DisplayName("EXPIRED 알림을 받으면 구독 상태를 EXPIRED로 업데이트한다")
    void whenExpiredNotification_thenUpdatesStateToExpired() {
        // given
        String jsonData = createNotificationJson(13, TEST_PURCHASE_TOKEN, TEST_SUBSCRIPTION_ID); // EXPIRED = 13
        PubSubMessageRequest pubSubMessage = createPubSubMessage(jsonData);

        doNothing().when(subscriptionUpdater).updateState(TEST_PURCHASE_TOKEN, SubscriptionState.EXPIRED);

        // when
        purchaseNotificationService.processPubSubMessage(pubSubMessage);

        // then
        verify(subscriptionUpdater).updateState(TEST_PURCHASE_TOKEN, SubscriptionState.EXPIRED);
    }

    @Test
    @DisplayName("RENEWED 알림을 받으면 구독 갱신을 처리한다")
    void whenRenewedNotification_thenProcessesSubscriptionRenewal() {
        // given
        String jsonData = createNotificationJson(2, TEST_PURCHASE_TOKEN, TEST_SUBSCRIPTION_ID); // RENEWED = 2
        PubSubMessageRequest pubSubMessage = createPubSubMessage(jsonData);

        Subscription existingSubscription = createSubscription(SubscriptionState.ACTIVE);
        Subscription renewedSubscription = createSubscription(SubscriptionState.ACTIVE);
        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo();

        given(googlePlayApiPort.getSubscriptionInfo(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN))
                .willReturn(subscriptionInfo);
        given(querySubscriptionPort.findByPurchaseToken(TEST_PURCHASE_TOKEN))
                .willReturn(Optional.of(existingSubscription));
        given(subscriptionFactory.renewedFrom(eq(existingSubscription), any(LocalDateTime.class)))
                .willReturn(renewedSubscription);

        // when
        purchaseNotificationService.processPubSubMessage(pubSubMessage);

        // then
        verify(googlePlayApiPort).getSubscriptionInfo(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN);
        verify(subscriptionSavePort).save(renewedSubscription);
    }

    @Test
    @DisplayName("RENEWED 알림에서 구독이 존재하지 않으면 갱신하지 않는다")
    void whenRenewedNotificationWithNoExistingSubscription_thenDoesNotRenew() {
        // given
        String jsonData = createNotificationJson(2, TEST_PURCHASE_TOKEN, TEST_SUBSCRIPTION_ID);
        PubSubMessageRequest pubSubMessage = createPubSubMessage(jsonData);

        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo();

        given(googlePlayApiPort.getSubscriptionInfo(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN))
                .willReturn(subscriptionInfo);
        given(querySubscriptionPort.findByPurchaseToken(TEST_PURCHASE_TOKEN))
                .willReturn(Optional.empty());

        // when
        purchaseNotificationService.processPubSubMessage(pubSubMessage);

        // then
        verify(subscriptionSavePort, never()).save(any());
    }

    @Test
    @DisplayName("잘못된 Base64 데이터인 경우 예외가 발생한다")
    void whenInvalidBase64Data_thenThrowsException() {
        // given
        PubSubMessageDataRequest messageData = new PubSubMessageDataRequest(
                new HashMap<>(),
                "invalid-base64!!!",
                "message-id-123"
        );
        PubSubMessageRequest pubSubMessage = new PubSubMessageRequest(messageData, "subscription-name");

        // when & then
        assertThatThrownBy(() -> purchaseNotificationService.processPubSubMessage(pubSubMessage))
                .isInstanceOf(PurchaseNotificationProcessingFailedException.class);
    }

    @Test
    @DisplayName("잘못된 JSON 형식인 경우 예외가 발생한다")
    void whenInvalidJsonFormat_thenThrowsException() {
        // given
        String invalidJson = "{ invalid json }";
        String encodedData = Base64.getEncoder().encodeToString(invalidJson.getBytes(StandardCharsets.UTF_8));

        PubSubMessageDataRequest messageData = new PubSubMessageDataRequest(
                new HashMap<>(),
                encodedData,
                "message-id-123"
        );
        PubSubMessageRequest pubSubMessage = new PubSubMessageRequest(messageData, "subscription-name");

        // when & then
        assertThatThrownBy(() -> purchaseNotificationService.processPubSubMessage(pubSubMessage))
                .isInstanceOf(PurchaseNotificationProcessingFailedException.class);
    }

    /**
     * 테스트용 알림 JSON 데이터를 생성합니다.
     *
     * @param notificationType 알림 타입
     * @param purchaseToken 구매 토큰
     * @param subscriptionId 구독 ID
     * @return Base64 인코딩된 JSON 문자열
     */
    private String createNotificationJson(int notificationType, String purchaseToken, String subscriptionId) {
        String json = String.format(
                "{\"version\":\"1.0\",\"packageName\":\"com.test.app\",\"eventTimeMillis\":1638360000000," +
                        "\"subscriptionNotification\":{\"version\":\"1.0\",\"notificationType\":%d," +
                        "\"purchaseToken\":\"%s\",\"subscriptionId\":\"%s\"}}",
                notificationType, purchaseToken, subscriptionId
        );
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 테스트용 PubSubMessageRequest 객체를 생성합니다.
     *
     * @param encodedData Base64 인코딩된 데이터
     * @return 생성된 PubSubMessageRequest 객체
     */
    private PubSubMessageRequest createPubSubMessage(String encodedData) {
        PubSubMessageDataRequest messageData = new PubSubMessageDataRequest(
                new HashMap<>(),
                encodedData,
                "message-id-123"
        );
        return new PubSubMessageRequest(messageData, "subscription-name");
    }

    /**
     * 테스트용 Subscription 객체를 생성합니다.
     *
     * @param state 구독 상태
     * @return 생성된 Subscription 객체
     */
    private Subscription createSubscription(SubscriptionState state) {
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
}
