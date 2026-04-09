package whispy_server.whispy.global.constants;

/**
 * 세션 저장 검증에 사용하는 상수 모음.
 */
public final class SessionValidationConstants {

    /**
     * 저장 가능한 최소 세션 지속 시간(초).
     */
    public static final int MIN_SESSION_DURATION_SECONDS = 60;

    private SessionValidationConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
}
