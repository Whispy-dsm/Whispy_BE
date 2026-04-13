package whispy_server.whispy.global.cache.key;

import java.lang.reflect.Method;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.cache.version.StatisticsCacheDomain;
import whispy_server.whispy.global.security.auth.AuthDetails;
import whispy_server.whispy.global.cache.version.StatisticsCacheVersionManager;

/**
 * 통계 요약 조회용 캐시 키 생성기.
 *
 * 키 구성: userId:period:date:v{version}
 */
@Component("statisticsSummaryKeyGenerator")
@RequiredArgsConstructor
public class StatisticsSummaryKeyGenerator implements KeyGenerator {

    private final StatisticsCacheVersionManager statisticsCacheVersionManager;

    @Override
    public Object generate(Object target, Method method, Object... params) {
        AuthDetails authDetails = (AuthDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = authDetails.id();
        Enum<?> period = (Enum<?>) params[0];
        LocalDate date = (LocalDate) params[1];
        StatisticsCacheDomain domain = resolveDomain(target);
        long version = statisticsCacheVersionManager.getUserVersion(userId, domain);

        return userId + ":" + period.name() + ":" + date + ":v" + version;
    }

    private StatisticsCacheDomain resolveDomain(Object target) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        String className = targetClass.getSimpleName();

        if (className.contains("Focus")) {
            return StatisticsCacheDomain.FOCUS;
        }
        if (className.contains("Sleep")) {
            return StatisticsCacheDomain.SLEEP;
        }
        if (className.contains("Meditation")) {
            return StatisticsCacheDomain.MEDITATION;
        }

        throw new IllegalArgumentException("Unsupported statistics cache target: " + targetClass.getName());
    }
}
