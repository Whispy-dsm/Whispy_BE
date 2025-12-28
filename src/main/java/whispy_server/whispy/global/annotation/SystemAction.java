package whispy_server.whispy.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 시스템 이벤트를 로깅하기 위한 커스텀 어노테이션.
 *
 * 외부 시스템(Google Play, FCM 등)에서 트리거되는 이벤트나
 * 스케줄러, 배치 작업 등 시스템 내부에서 자동으로 실행되는 작업을 로깅합니다.
 * 사용자가 직접 시작한 작업은 @UserAction을 사용해야 합니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemAction {
    /**
     * 시스템 이벤트 설명
     *
     * @return 이벤트 설명 (예: "Pub/Sub 구매 알림 처리", "일일 통계 집계")
     */
    String value();
}
