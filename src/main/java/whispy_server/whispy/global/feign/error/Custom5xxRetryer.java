package whispy_server.whispy.global.feign.error;

import feign.RetryableException;
import feign.Retryer;

/**
 * 5xx 응답에 대해 지정된 횟수만큼 간단히 재시도하는 Retryer 구현.
 */
public class Custom5xxRetryer implements Retryer {

    private final int maxAttempts; // 최대 재시도
    private final long backoff; // 대시 시간
    private int attempt; // 현재 시도 횟수

    /**
     * 기본 설정으로 Retryer를 생성합니다.
     *
     * 기본값: 최대 2회 재시도, 100ms 대기 시간
     */
    public Custom5xxRetryer(){
        this(2, 100L);
    }

    /**
     * 커스텀 설정으로 Retryer를 생성합니다.
     *
     * @param maxAttempts 최대 재시도 횟수
     * @param backoff     재시도 간 대기 시간 (밀리초)
     */
    public Custom5xxRetryer(int maxAttempts, long backoff){
        this.maxAttempts=maxAttempts;
        this.backoff=backoff;
        this.attempt = 1;
    }

    /**
     * 재시도 횟수를 초과하면 예외를 다시 던지고, 그렇지 않으면 지연 후 재시도한다.
     */
    @Override
    public void continueOrPropagate(RetryableException e){
        if(attempt++ >= maxAttempts){
            throw e;
        }

        try {
            Thread.sleep(backoff);
        } catch (InterruptedException exception){
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 새로운 Retryer 인스턴스를 복제합니다.
     *
     * Feign은 각 요청마다 새로운 Retryer 인스턴스가 필요하므로
     * 동일한 설정으로 새 객체를 생성하여 반환합니다.
     *
     * @return 동일한 설정의 새 Retryer 인스턴스
     */
    @Override
    public Retryer clone() {
        return new Custom5xxRetryer(maxAttempts,backoff);
    }
}
