package whispy_server.whispy.domain.meditationsession.model.types;

/**
 * 명상 호흡 모드 열거형.
 *
 * 명상 세션을 분류하기 위한 호흡 모드입니다.
 * 사용자가 어떤 종류의 호흡 기법으로 명상했는지 추적할 수 있습니다.
 */
public enum BreatheMode {
    /** 빠른 호흡 (카팔라 바티) */
    RAPID_BREATHING,
    /** 4-7-8 호흡법 */
    FOUR_SEVEN_EIGHT,
    /** 교대 호흡 (나디 쇼드하나) */
    ALTERNATE_NOSTRIL,
    /** 박스 호흡 (4-4-4-4 호흡) */
    BOX_BREATHING,
    /** 복식 호흡 (복부 호흡) */
    DIAPHRAGMATIC
}
