package whispy_server.whispy.global.utils.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security 관련 유틸리티 클래스.
 *
 * 인증된 사용자 정보 조회 등의 공통 기능을 제공합니다.
 */
public final class SecurityUtil {

    private SecurityUtil() {
        throw new AssertionError("Utility class");
    }

    /**
     * 현재 요청의 사용자 식별자를 반환합니다.
     *
     * @return 인증된 사용자의 ID (Long을 String으로 변환), 또는 익명 사용자인 경우 "ANONYMOUS"
     */
    public static String getCurrentUserIdentifier() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return "ANONYMOUS";
    }
}
