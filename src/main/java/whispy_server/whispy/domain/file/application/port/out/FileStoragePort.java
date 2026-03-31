package whispy_server.whispy.domain.file.application.port.out;

import java.io.InputStream;

/**
 * 파일 저장소 연동을 추상화한 outbound port.
 */
public interface FileStoragePort {

    /**
     * 지정된 object key 위치에 파일을 업로드합니다.
     *
     * @param objectKey 저장소 내부에서 사용할 객체 키
     * @param contentType 저장할 파일의 MIME 타입
     * @param inputStream 업로드할 파일 데이터 스트림
     * @param contentLength 업로드할 파일 바이트 수
     */
    void upload(String objectKey, String contentType, InputStream inputStream, long contentLength);

    /**
     * object key에 해당하는 파일을 다운로드합니다.
     *
     * @param objectKey 조회할 객체 키
     * @return 파일 스트림과 메타데이터
     */
    StoredFile download(String objectKey);

    /**
     * object key에 해당하는 파일을 삭제합니다.
     *
     * @param objectKey 삭제할 객체 키
     */
    void delete(String objectKey);
}
