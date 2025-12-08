package whispy_server.whispy.domain.user.model.types;

/**
 * 사용자 인증 제공자(OAuth Provider)를 나타내는 열거형.
 * 소셜 로그인 또는 로컬 회원가입 유형을 구분합니다.
 */
public enum Provider {
    /** 구글 OAuth 로그인 */
    GOOGLE,
    /** 카카오 OAuth 로그인 */
    KAKAO,
    /** 로컬 회원가입 (이메일/비밀번호) */
    LOCAL
}
