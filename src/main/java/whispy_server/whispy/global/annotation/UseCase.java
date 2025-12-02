package whispy_server.whispy.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 애플리케이션 계층의 UseCase 구현체임을 명시하기 위한 커스텀 애노테이션이다.
 * 컴포넌트 스캔이나 AOP 설정에서 특정 계층을 선별할 때 사용한다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseCase {
}
