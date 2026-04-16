package whispy_server.whispy.domain.payment.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import whispy_server.whispy.domain.payment.adapter.in.web.dto.response.CheckUserSubscriptionStatusResponse;
import whispy_server.whispy.domain.payment.application.port.out.QuerySubscriptionPort;
import whispy_server.whispy.domain.payment.application.service.CheckUserSubscriptionStatusService;
import whispy_server.whispy.domain.payment.model.Subscription;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.domain.user.model.User;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CheckUserSubscriptionStatusService unit tests")
class CheckUserSubscriptionStatusServiceTest {

    @InjectMocks
    private CheckUserSubscriptionStatusService checkUserSubscriptionStatusService;

    @Mock
    private QuerySubscriptionPort querySubscriptionPort;

    @Mock
    private UserFacadeUseCase userFacadeUseCase;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String AUTHENTICATED_EMAIL = "authenticated@example.com";
    private static final String TEST_PURCHASE_TOKEN = "test-token-12345";

    @Test
    @DisplayName("returns true for active subscriptions before expiry")
    void whenActiveSubscriptionExists_thenReturnsTrue() {
        Subscription activeSubscription = createSubscription(
                TEST_EMAIL,
                SubscriptionState.ACTIVE,
                LocalDateTime.now().plusDays(30)
        );

        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findCurrentSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(activeSubscription));

        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed();

        assertThat(response.isSubscribed()).isTrue();
    }

    @Test
    @DisplayName("returns false for expired active subscriptions")
    void whenActiveSubscriptionExpired_thenReturnsFalse() {
        Subscription expiredSubscription = createSubscription(
                TEST_EMAIL,
                SubscriptionState.ACTIVE,
                LocalDateTime.now().minusDays(1)
        );

        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findCurrentSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(expiredSubscription));

        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed();

        assertThat(response.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("returns false when there is no subscription")
    void whenNoSubscriptionExists_thenReturnsFalse() {
        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findCurrentSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.empty());

        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed();

        assertThat(response.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("returns true for canceled subscriptions before expiry")
    void whenCanceledButNotExpired_thenReturnsTrue() {
        Subscription canceledSubscription = createSubscription(
                TEST_EMAIL,
                SubscriptionState.CANCELED,
                LocalDateTime.now().plusDays(10)
        );

        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findCurrentSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(canceledSubscription));

        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed();

        assertThat(response.isSubscribed()).isTrue();
    }

    @Test
    @DisplayName("returns true for grace period subscriptions before expiry")
    void whenGracePeriodAndNotExpired_thenReturnsTrue() {
        Subscription gracePeriodSubscription = createSubscription(
                TEST_EMAIL,
                SubscriptionState.GRACE_PERIOD,
                LocalDateTime.now().plusDays(3)
        );

        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findCurrentSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(gracePeriodSubscription));

        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed();

        assertThat(response.isSubscribed()).isTrue();
    }

    @Test
    @DisplayName("returns false for expired subscriptions")
    void whenExpiredState_thenReturnsFalse() {
        Subscription expiredSubscription = createSubscription(
                TEST_EMAIL,
                SubscriptionState.EXPIRED,
                LocalDateTime.now().minusDays(1)
        );

        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findCurrentSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(expiredSubscription));

        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed();

        assertThat(response.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("returns false for pending subscriptions")
    void whenPendingState_thenReturnsFalse() {
        Subscription pendingSubscription = createSubscription(
                TEST_EMAIL,
                SubscriptionState.PENDING,
                LocalDateTime.now().plusDays(30)
        );

        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findCurrentSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(pendingSubscription));

        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed();

        assertThat(response.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("returns false for on hold subscriptions")
    void whenOnHoldState_thenReturnsFalse() {
        Subscription onHoldSubscription = createSubscription(
                TEST_EMAIL,
                SubscriptionState.ON_HOLD,
                LocalDateTime.now().plusDays(30)
        );

        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findCurrentSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(onHoldSubscription));

        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed();

        assertThat(response.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("returns false for revoked subscriptions")
    void whenRevokedState_thenReturnsFalse() {
        Subscription revokedSubscription = createSubscription(
                TEST_EMAIL,
                SubscriptionState.REVOKED,
                LocalDateTime.now().plusDays(30)
        );

        given(userFacadeUseCase.currentUser()).willReturn(createUser());
        given(querySubscriptionPort.findCurrentSubscriptionByEmail(TEST_EMAIL))
                .willReturn(Optional.of(revokedSubscription));

        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed();

        assertThat(response.isSubscribed()).isFalse();
    }

    @Test
    @DisplayName("uses the authenticated user email for subscription status lookup")
    void whenCheckingSubscriptionStatus_thenUsesAuthenticatedUserEmail() {
        Subscription activeSubscription = createSubscription(
                AUTHENTICATED_EMAIL,
                SubscriptionState.ACTIVE,
                LocalDateTime.now().plusDays(30)
        );
        given(userFacadeUseCase.currentUser()).willReturn(createUser(AUTHENTICATED_EMAIL));
        given(querySubscriptionPort.findCurrentSubscriptionByEmail(AUTHENTICATED_EMAIL))
                .willReturn(Optional.of(activeSubscription));

        CheckUserSubscriptionStatusResponse response = checkUserSubscriptionStatusService.isUserSubscribed(TEST_EMAIL);

        assertThat(response.isSubscribed()).isTrue();
        verify(userFacadeUseCase).currentUser();
        verify(querySubscriptionPort).findCurrentSubscriptionByEmail(AUTHENTICATED_EMAIL);
        verify(querySubscriptionPort, never()).findCurrentSubscriptionByEmail(TEST_EMAIL);
    }

    private Subscription createSubscription(String email, SubscriptionState state, LocalDateTime expiryTime) {
        return new Subscription(
                1L,
                email,
                TEST_PURCHASE_TOKEN,
                ProductType.MONTHLY,
                LocalDateTime.now().minusDays(15),
                state,
                true,
                expiryTime
        );
    }

    private User createUser() {
        return createUser(TEST_EMAIL);
    }

    private User createUser(String email) {
        return new User(
                1L,
                email,
                "password123!",
                "Test User",
                "https://example.com/profile.jpg",
                Gender.FEMALE,
                Role.USER,
                "LOCAL",
                null,
                LocalDateTime.now().minusDays(30)
        );
    }
}
