package whispy_server.whispy.global.feign.error;

import feign.RetryableException;
import feign.Retryer;

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
