package whispy_server.whispy.domain.sleepsession.application.port.out;

import whispy_server.whispy.domain.sleepsession.model.SleepSession;

/**
 * 수면 세션 저장 포트 인터페이스.
 *
 * 수면 세션 저장 작업을 정의하는 아웃바운드 포트입니다.
 */
public interface SleepSessionSavePort {

    /**
     * 수면 세션을 저장합니다.
     *
     * @param session 저장할 도메인 모델
     * @return 저장된 세션의 도메인 모델 (ID 포함)
     */
    SleepSession save(SleepSession session);
}
