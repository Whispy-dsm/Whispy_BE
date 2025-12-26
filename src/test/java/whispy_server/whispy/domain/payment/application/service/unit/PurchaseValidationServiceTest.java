package whispy_server.whispy.domain.payment.application.service.unit;
import whispy_server.whispy.domain.payment.application.service.PurchaseValidationService;
import whispy_server.whispy.domain.payment.application.service.PurchaseProcessingService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.request.ValidatePurchaseRequest;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.ValidatePurchaseResponse;
import whispy_server.whispy.domain.payment.application.port.out.GooglePlayApiPort;
import whispy_server.whispy.domain.payment.model.GooglePlaySubscriptionInfo;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.exception.domain.payment.InvalidPaymentStateException;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * PurchaseValidationService의 단위 테스트 클래스
 *
 * 구매 검증 서비스의 다양한 시나리오를 검증합니다.
 * Google Play 구매 검증 및 결제 상태 확인을 테스트합니다.
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PurchaseValidationService 테스트")
class PurchaseValidationServiceTest {

    @InjectMocks
    private PurchaseValidationService purchaseValidationService;

    @Mock
    private GooglePlayApiPort googlePlayApiPort;

    @Mock
    private PurchaseProcessingService purchaseProcessingService;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PURCHASE_TOKEN = "test-token-12345";
    private static final String TEST_SUBSCRIPTION_ID = "monthly_subscription";
    private static final Long TEST_USER_ID = 1L;

    @Test
    @DisplayName("유효한 구매인 경우 검증 성공 및 처리가 진행된다")
    void whenValidPurchase_thenValidatesAndProcesses() {
        // given
        ValidatePurchaseRequest request = createValidatePurchaseRequest();
        User user = createUser();
        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo(1); // paymentState = 1 (결제 완료)
        ValidatePurchaseResponse expectedResponse = new ValidatePurchaseResponse(true);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(googlePlayApiPort.getSubscriptionInfo(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN))
                .willReturn(subscriptionInfo);
        given(purchaseProcessingService.processValidatedPurchase(
                eq(TEST_EMAIL),
                eq(TEST_PURCHASE_TOKEN),
                eq(TEST_SUBSCRIPTION_ID),
                eq(subscriptionInfo)
        )).willReturn(expectedResponse);

        // when
        ValidatePurchaseResponse response = purchaseValidationService.validateAndProcessPurchase(request);

        // then
        assertThat(response.isValid()).isTrue();
        verify(purchaseProcessingService).processValidatedPurchase(
                TEST_EMAIL,
                TEST_PURCHASE_TOKEN,
                TEST_SUBSCRIPTION_ID,
                subscriptionInfo
        );
    }

    @Test
    @DisplayName("결제 상태가 유효하지 않은 경우 예외가 발생한다")
    void whenInvalidPaymentState_thenThrowsException() {
        // given
        ValidatePurchaseRequest request = createValidatePurchaseRequest();
        User user = createUser();
        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo(0); // paymentState = 0 (결제 미완료)

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(googlePlayApiPort.getSubscriptionInfo(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN))
                .willReturn(subscriptionInfo);

        // when & then
        assertThatThrownBy(() -> purchaseValidationService.validateAndProcessPurchase(request))
                .isInstanceOf(InvalidPaymentStateException.class);
    }

    @Test
    @DisplayName("결제 대기 상태인 경우 예외가 발생한다")
    void whenPaymentPending_thenThrowsException() {
        // given
        ValidatePurchaseRequest request = createValidatePurchaseRequest();
        User user = createUser();
        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo(2); // paymentState = 2 (결제 대기)

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(googlePlayApiPort.getSubscriptionInfo(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN))
                .willReturn(subscriptionInfo);

        // when & then
        assertThatThrownBy(() -> purchaseValidationService.validateAndProcessPurchase(request))
                .isInstanceOf(InvalidPaymentStateException.class);
    }

    @Test
    @DisplayName("유효한 구매 검증 시 현재 사용자 정보를 조회한다")
    void whenValidatingPurchase_thenRetrievesCurrentUser() {
        // given
        ValidatePurchaseRequest request = createValidatePurchaseRequest();
        User user = createUser();
        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo(1);
        ValidatePurchaseResponse expectedResponse = new ValidatePurchaseResponse(true);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(googlePlayApiPort.getSubscriptionInfo(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN))
                .willReturn(subscriptionInfo);
        given(purchaseProcessingService.processValidatedPurchase(anyString(), anyString(), anyString(), any()))
                .willReturn(expectedResponse);

        // when
        purchaseValidationService.validateAndProcessPurchase(request);

        // then
        verify(userFacadeUseCase).currentUser();
    }

    @Test
    @DisplayName("구매 검증 시 Google Play API를 호출한다")
    void whenValidatingPurchase_thenCallsGooglePlayApi() {
        // given
        ValidatePurchaseRequest request = createValidatePurchaseRequest();
        User user = createUser();
        GooglePlaySubscriptionInfo subscriptionInfo = createSubscriptionInfo(1);
        ValidatePurchaseResponse expectedResponse = new ValidatePurchaseResponse(true);

        given(userFacadeUseCase.currentUser()).willReturn(user);
        given(googlePlayApiPort.getSubscriptionInfo(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN))
                .willReturn(subscriptionInfo);
        given(purchaseProcessingService.processValidatedPurchase(anyString(), anyString(), anyString(), any()))
                .willReturn(expectedResponse);

        // when
        purchaseValidationService.validateAndProcessPurchase(request);

        // then
        verify(googlePlayApiPort).getSubscriptionInfo(TEST_SUBSCRIPTION_ID, TEST_PURCHASE_TOKEN);
    }

    /**
     * 테스트용 ValidatePurchaseRequest 객체를 생성합니다.
     *
     * @return 생성된 ValidatePurchaseRequest 객체
     */
    private ValidatePurchaseRequest createValidatePurchaseRequest() {
        return new ValidatePurchaseRequest(
                TEST_PURCHASE_TOKEN,
                TEST_SUBSCRIPTION_ID
        );
    }

    /**
     * 테스트용 User 객체를 생성합니다.
     *
     * @return 생성된 User 객체
     */
    private User createUser() {
        return new User(
                TEST_USER_ID,
                TEST_EMAIL,
                "password",
                "TestUser",
                null,
                Gender.MALE,
                Role.USER,
                null,
                null,
                LocalDateTime.now()
        );
    }

    /**
     * 테스트용 GooglePlaySubscriptionInfo 객체를 생성합니다.
     *
     * @param paymentState 결제 상태 (0: 결제 미완료, 1: 결제 완료, 2: 무료 체험)
     * @return 생성된 GooglePlaySubscriptionInfo 객체
     */
    private GooglePlaySubscriptionInfo createSubscriptionInfo(int paymentState) {
        long currentTime = System.currentTimeMillis();
        return new GooglePlaySubscriptionInfo(
                TEST_PURCHASE_TOKEN,
                null,
                paymentState,
                currentTime + (30L * 24 * 60 * 60 * 1000), // 30일 후
                currentTime,
                null
        );
    }
}
