package whispy_server.whispy.global.config.cache;

import java.lang.reflect.Method;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.user.application.port.in.UserFacadeUseCase;
import whispy_server.whispy.global.utils.redis.StatisticsCacheVersionManager;

/**
 * 통계 요약 조회용 캐시 키 생성기.
 *
 * 키 구성: userId:period:date:v{version}
 */
@Component("statisticsSummaryKeyGenerator")
@RequiredArgsConstructor
public class StatisticsSummaryKeyGenerator implements KeyGenerator {

    private final UserFacadeUseCase userFacadeUseCase;
    private final StatisticsCacheVersionManager statisticsCacheVersionManager;

    @Override
    public Object generate(Object target, Method method, Object... params) {
        Long userId = userFacadeUseCase.currentUser().id();
        Enum<?> period = (Enum<?>) params[0];
        LocalDate date = (LocalDate) params[1];
        long version = statisticsCacheVersionManager.getUserVersion(userId);

        return userId + ":" + period.name() + ":" + date + ":v" + version;
    }
}
