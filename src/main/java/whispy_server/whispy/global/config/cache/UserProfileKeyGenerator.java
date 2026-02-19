package whispy_server.whispy.global.config.cache;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import whispy_server.whispy.global.security.auth.AuthDetails;

/**
 * 내 프로필 조회 캐시 키 생성기.
 *
 * 키 구성: userId
 */
@Component("userProfileKeyGenerator")
public class UserProfileKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        AuthDetails authDetails = (AuthDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return authDetails.id();
    }
}
