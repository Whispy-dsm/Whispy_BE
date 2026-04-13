package whispy_server.whispy.global.cache.key.unit;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import whispy_server.whispy.domain.statistics.focus.types.FocusPeriodType;
import whispy_server.whispy.global.cache.key.StatisticsSummaryKeyGenerator;
import whispy_server.whispy.global.cache.version.StatisticsCacheDomain;
import whispy_server.whispy.global.cache.version.StatisticsCacheVersionManager;
import whispy_server.whispy.global.exception.domain.statistics.UnsupportedStatisticsCacheTargetException;
import whispy_server.whispy.global.security.auth.AuthDetails;
import whispy_server.whispy.global.security.jwt.domain.entity.types.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatisticsSummaryKeyGenerator 테스트")
class StatisticsSummaryKeyGeneratorTest {

    private static final Long USER_ID = 1L;

    @Mock
    private StatisticsCacheVersionManager statisticsCacheVersionManager;

    private StatisticsSummaryKeyGenerator generator;

    @BeforeEach
    void setUp() {
        AuthDetails authDetails = new AuthDetails(USER_ID, Role.USER.name(), Collections.emptyMap());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authDetails, null, authDetails.getAuthorities())
        );
        generator = new StatisticsSummaryKeyGenerator(statisticsCacheVersionManager);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("focus 대상은 focus 도메인 버전을 사용해 캐시 키를 생성한다")
    void generate_usesFocusDomainVersion() throws NoSuchMethodException {
        LocalDate date = LocalDate.of(2024, 1, 15);
        Method method = Object.class.getMethod("toString");
        when(statisticsCacheVersionManager.getUserVersion(USER_ID, StatisticsCacheDomain.FOCUS)).thenReturn(3L);

        Object key = generator.generate(new FocusStatisticsTarget(), method, FocusPeriodType.WEEK, date);

        assertThat(key).isEqualTo("1:WEEK:2024-01-15:v3");
        verify(statisticsCacheVersionManager).getUserVersion(USER_ID, StatisticsCacheDomain.FOCUS);
    }

    @Test
    @DisplayName("지원하지 않는 대상 타입이면 custom exception을 던진다")
    void generate_throwsCustomExceptionWhenTargetIsUnsupported() throws NoSuchMethodException {
        Method method = Object.class.getMethod("toString");

        assertThatThrownBy(() -> generator.generate(
                new UnknownStatisticsTarget(),
                method,
                FocusPeriodType.WEEK,
                LocalDate.of(2024, 1, 15)
        )).isSameAs(UnsupportedStatisticsCacheTargetException.EXCEPTION);
    }

    private static final class FocusStatisticsTarget {
    }

    private static final class UnknownStatisticsTarget {
    }
}
