package whispy_server.whispy.domain.focussession.model.types;

/**
 * 집중 활동 태그 열거형.
 *
 * 집중 세션을 분류하기 위한 태그입니다.
 * 사용자가 어떤 종류의 활동에 집중했는지 추적할 수 있습니다.
 */
public enum FocusTag {
    /** 업무 관련 활동 */
    WORK,
    /** 공부 관련 활동 */
    STUDY,
    /** 독서 활동 */
    READ,
    /** 명상 활동 */
    MEDITATION,
    /** 스포츠/신체 활동 */
    SPORT
}
