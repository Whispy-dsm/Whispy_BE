package whispy_server.whispy.domain.user.adapter.in.web.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import whispy_server.whispy.global.support.IntegrationTestSupport;
import whispy_server.whispy.domain.user.adapter.in.web.dto.request.UserLoginRequest;
import whispy_server.whispy.domain.user.adapter.out.entity.UserJpaEntity;
import whispy_server.whispy.domain.user.adapter.out.persistence.repository.UserRepository;
import whispy_server.whispy.domain.user.model.types.Gender;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;
import whispy_server.whispy.global.security.jwt.domain.repository.RefreshTokenRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 사용자 로그인 통합 테스트.
 *
 * H2 인메모리 DB를 사용하여 전체 로그인 플로우를 검증합니다.
 * 각 테스트는 독립적으로 실행되며, IntegrationTestSupport를 상속받아 공통 설정을 재사용합니다.
 */
@DisplayName("사용자 로그인 통합 테스트")
class UserLoginIntegrationTest extends IntegrationTestSupport {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private RefreshTokenRepository refreshTokenRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        private static final String TEST_EMAIL = "test@example.com";
        private static final String TEST_PASSWORD = "password123!";
        private static final String TEST_FCM_TOKEN = "test-fcm-token-12345";

        @BeforeEach
        void setUp() {
                // 테스트용 사용자 생성
                UserJpaEntity user = UserJpaEntity.builder()
                                .email(TEST_EMAIL)
                                .password(passwordEncoder.encode(TEST_PASSWORD))
                                .name("TestUser")
                                .profileImageUrl("https://example.com/default.jpg")
                                .gender(Gender.MALE)
                                .role(Role.USER)
                                .provider("일반 로그인")
                                .fcmToken(null)
                                .build();

                userRepository.save(user);
        }

        @Test
        @DisplayName("유효한 이메일과 비밀번호로 로그인에 성공한다")
        void whenValidCredentials_thenLoginSuccessfully() throws Exception {
                // given
                UserLoginRequest request = new UserLoginRequest(
                                TEST_EMAIL,
                                TEST_PASSWORD,
                                TEST_FCM_TOKEN);

                // when
                ResultActions result = mockMvc.perform(post("/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)));

                // then
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.access_token", notNullValue()))
                                .andExpect(jsonPath("$.refresh_token", notNullValue()))
                                .andDo(print());
        }

        @Test
        @DisplayName("존재하지 않는 이메일로 로그인 시 실패한다")
        void whenEmailNotFound_thenLoginFails() throws Exception {
                // given
                UserLoginRequest request = new UserLoginRequest(
                                "nonexistent@example.com",
                                TEST_PASSWORD,
                                TEST_FCM_TOKEN);

                // when
                ResultActions result = mockMvc.perform(post("/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)));

                // then
                result.andExpect(status().isNotFound())
                                .andDo(print());
        }

        @Test
        @DisplayName("잘못된 비밀번호로 로그인 시 실패한다")
        void whenPasswordMismatch_thenLoginFails() throws Exception {
                // given
                UserLoginRequest request = new UserLoginRequest(
                                TEST_EMAIL,
                                "wrongPassword123!",
                                TEST_FCM_TOKEN);

                // when
                ResultActions result = mockMvc.perform(post("/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)));

                // then
                result.andExpect(status().isUnauthorized())
                                .andDo(print());
        }

        @Test
        @DisplayName("로그인 시 FCM 토큰이 업데이트된다")
        void whenLogin_thenFcmTokenUpdated() throws Exception {
                // given
                UserLoginRequest request = new UserLoginRequest(
                                TEST_EMAIL,
                                TEST_PASSWORD,
                                TEST_FCM_TOKEN);

                // when
                mockMvc.perform(post("/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk());

                // then
                UserJpaEntity updatedUser = userRepository.findByEmail(TEST_EMAIL).orElseThrow();
                assertThat(updatedUser.getFcmToken()).isEqualTo(TEST_FCM_TOKEN);
        }

        @Test
        @DisplayName("재로그인 시 기존 Refresh Token은 삭제되고 새로운 토큰이 발급된다")
        void whenReLogin_thenOldRefreshTokenDeletedAndNewTokenIssued() throws Exception {
                // given
                UserLoginRequest request = new UserLoginRequest(
                                TEST_EMAIL,
                                TEST_PASSWORD,
                                TEST_FCM_TOKEN);

                // 첫 번째 로그인
                mockMvc.perform(post("/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk());

                UserJpaEntity user = userRepository.findByEmail(TEST_EMAIL).orElseThrow();
                Long userId = user.getId();

                // 첫 번째 Refresh Token 저장
                String firstRefreshToken = refreshTokenRepository.findById(userId)
                                .orElseThrow()
                                .getToken();

                // 토큰 발급 시간을 다르게 하기 위해 1초 대기
                Thread.sleep(1000);

                // when - 두 번째 로그인 (기존 세션 무효화)
                mockMvc.perform(post("/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk());

                // then - 새로운 Refresh Token이 발급되었는지 확인
                String secondRefreshToken = refreshTokenRepository.findById(userId)
                                .orElseThrow()
                                .getToken();

                assertThat(secondRefreshToken)
                                .isNotEqualTo(firstRefreshToken)
                                .as("재로그인 시 새로운 Refresh Token이 발급되어야 합니다");
        }

        @Test
        @DisplayName("유효하지 않은 이메일 형식으로 로그인 시 실패한다")
        void whenInvalidEmailFormat_thenValidationFails() throws Exception {
                // given
                UserLoginRequest request = new UserLoginRequest(
                                "invalid-email",
                                TEST_PASSWORD,
                                TEST_FCM_TOKEN);

                // when
                ResultActions result = mockMvc.perform(post("/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)));

                // then
                result.andExpect(status().isBadRequest())
                                .andDo(print());
        }

        @Test
        @DisplayName("비밀번호 길이가 8자 미만일 경우 유효성 검증 실패")
        void whenPasswordTooShort_thenValidationFails() throws Exception {
                // given
                UserLoginRequest request = new UserLoginRequest(
                                TEST_EMAIL,
                                "short",
                                TEST_FCM_TOKEN);

                // when
                ResultActions result = mockMvc.perform(post("/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)));

                // then
                result.andExpect(status().isBadRequest())
                                .andDo(print());
        }

        @Test
        @DisplayName("FCM 토큰이 빈 문자열일 경우 유효성 검증 실패")
        void whenFcmTokenBlank_thenValidationFails() throws Exception {
                // given
                UserLoginRequest request = new UserLoginRequest(
                                TEST_EMAIL,
                                TEST_PASSWORD,
                                "");

                // when
                ResultActions result = mockMvc.perform(post("/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)));

                // then
                result.andExpect(status().isBadRequest())
                                .andDo(print());
        }
}
