package whispy_server.whispy.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 도메인 애그리거트 루트를 표시하는 메타 애노테이션.
 *
 * DDD 관점에서 Aggregate 경계를 명시하기 위해 사용한다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Aggregate {
}
