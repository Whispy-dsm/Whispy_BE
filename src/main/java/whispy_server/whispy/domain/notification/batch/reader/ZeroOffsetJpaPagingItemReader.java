package whispy_server.whispy.domain.notification.batch.reader;

import org.springframework.batch.item.database.JpaPagingItemReader;

/**
 * 삭제 작업 시 OFFSET 기반 페이징 문제를 해결하는 커스텀 Reader.
 *
 * 일반 JpaPagingItemReader는 페이지를 0, 1, 2... 순으로 증가시키면서 읽습니다.
 * 하지만 삭제 작업과 함께 사용하면, 삭제된 데이터 수만큼 다음 데이터들이 앞으로 당겨지면서
 * OFFSET 계산이 어긋나 약 50%의 데이터가 누락되는 문제가 발생합니다.
 * 항상 0페이지(OFFSET 0)만 읽도록 고정하여 이 문제를 해결합니다.
 *
 * @param <T> 읽어올 엔티티 타입
 * @see org.springframework.batch.item.database.JpaPagingItemReader
 */
public class ZeroOffsetJpaPagingItemReader<T> extends JpaPagingItemReader<T> {

    /**
     * 페이지 번호를 항상 0으로 고정하여 OFFSET 0에서만 데이터를 읽습니다.
     */
    @Override
    protected void doReadPage() {
        setCurrentItemCount(0);
        super.doReadPage();
    }

    /**
     * 현재 페이지 번호를 반환합니다.
     *
     * @return 페이지 번호 (항상 0)
     */
    @Override
    public int getPage() {
        return 0;
    }
}
