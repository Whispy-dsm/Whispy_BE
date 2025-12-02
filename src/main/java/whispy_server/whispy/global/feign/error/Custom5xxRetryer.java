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

    public Custom5xxRetryer(){
        this(2, 100L);
    }


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

    @Override
    public Retryer clone() {
        return new Custom5xxRetryer(maxAttempts,backoff);
    }
}
