package whispy_server.whispy.domain.payment.adapter.in.web.controller.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import whispy_server.whispy.domain.payment.adapter.out.entity.SubscriptionJpaEntity;
import whispy_server.whispy.domain.payment.adapter.out.persistence.repository.SubscriptionJpaRepository;
import whispy_server.whispy.domain.payment.model.type.ProductType;
import whispy_server.whispy.domain.payment.model.type.SubscriptionState;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UserLoginRequest;
import whispy_server.whispy.domain.user.adapter.out.entity.UserJpaEntity;
import whispy_server.whispy.domain.user.adapter.out.persistence.repository.UserRepository;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.support.IntegrationTestSupport;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SubscriptionController integration tests")
class SubscriptionControllerIntegrationTest extends IntegrationTestSupport {

    private static final String TEST_EMAIL = "subscriber@example.com";
    private static final String TEST_PASSWORD = "password123!";
    private static final String TEST_FCM_TOKEN = "subscription-fcm-token";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionJpaRepository subscriptionJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        UserJpaEntity user = UserJpaEntity.builder()
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .name("Subscriber")
                .profileImageUrl("https://example.com/subscriber.jpg")
                .gender(Gender.FEMALE)
                .role(Role.USER)
                .provider("LOCAL")
                .fcmToken(null)
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("GET /subscriptions/me returns the authenticated user's subscription without purchase token exposure")
    void whenAuthenticated_thenReturnsMySubscriptionWithoutPurchaseToken() throws Exception {
        subscriptionJpaRepository.save(SubscriptionJpaEntity.builder()
                .email(TEST_EMAIL)
                .purchaseToken("sensitive-token-123")
                .productType(ProductType.MONTHLY)
                .purchaseTime(LocalDateTime.now().minusDays(3))
                .subscriptionState(SubscriptionState.ACTIVE)
                .autoRenewing(true)
                .expiryTime(LocalDateTime.now().plusDays(27))
                .build());

        String accessToken = loginAndExtractAccessToken();

        mockMvc.perform(get("/subscriptions/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscriptions.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.subscriptions.product_type").value("MONTHLY"))
                .andExpect(jsonPath("$.subscriptions.purchase_time", notNullValue()))
                .andExpect(jsonPath("$.subscriptions.expiry_time", notNullValue()))
                .andExpect(jsonPath("$.subscriptions.purchase_token").doesNotExist());
    }

    @Test
    @DisplayName("GET /subscriptions/me/status uses the authenticated user's current entitlement states")
    void whenGracePeriodSubscriptionExists_thenStatusEndpointReturnsSubscribed() throws Exception {
        subscriptionJpaRepository.save(SubscriptionJpaEntity.builder()
                .email(TEST_EMAIL)
                .purchaseToken("grace-token-123")
                .productType(ProductType.MONTHLY)
                .purchaseTime(LocalDateTime.now().minusDays(20))
                .subscriptionState(SubscriptionState.GRACE_PERIOD)
                .autoRenewing(false)
                .expiryTime(LocalDateTime.now().plusDays(5))
                .build());

        String accessToken = loginAndExtractAccessToken();

        mockMvc.perform(get("/subscriptions/me/status")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_subscribed").value(true));
    }

    @Test
    @DisplayName("legacy email path endpoints are not exposed")
    void legacyEmailPathEndpoints_areNotExposed() throws Exception {
        String accessToken = loginAndExtractAccessToken();

        mockMvc.perform(get("/subscriptions/user/" + TEST_EMAIL)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/subscriptions/user/" + TEST_EMAIL + "/status")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }

    private String loginAndExtractAccessToken() throws Exception {
        UserLoginRequest request = new UserLoginRequest(
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_FCM_TOKEN
        );

        ResultActions result = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", notNullValue()));

        String responseBody = result.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }
}
