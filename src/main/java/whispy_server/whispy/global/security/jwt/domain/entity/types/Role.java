package whispy_server.whispy.global.security.jwt.domain.entity.types;

/**
 * 인증된 계정에 부여되는 권한 체계를 정의한다.
 * Spring Security 권한명과 연계되어 접근 제어에 사용된다.
 */
public enum Role {
    USER,
    PREMIUM_USER,
    ADMIN

}
