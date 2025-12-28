package whispy_server.whispy.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 사용자 행동을 로깅하기 위한 커스텀 어노테이션.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAction {
    /**
     * 사용자 행동 설명
     *
     * @return 행동 설명 (예: "음악 좋아요", "수면 세션 저장")
     */
    String value();
}
