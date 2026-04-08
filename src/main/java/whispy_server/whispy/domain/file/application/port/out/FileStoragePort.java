package whispy_server.whispy.domain.file.application.port.out;

import org.springframework.core.io.InputStreamSource;

/**
 * 파일 저장소 연동을 추상화한 outbound port.
 */
public interface FileStoragePort {

    /**
     * 지정된 object key 위치에 파일을 업로드합니다.
     *
     * @param objectKey 저장소 내부에서 사용할 객체 키
     * @param contentType 저장할 파일의 MIME 타입
     * @param inputStreamSource 업로드 시마다 새 스트림을 제공하는 공급자
     * @param contentLength 업로드할 파일 바이트 수
     */
    void upload(String objectKey, String contentType, InputStreamSource inputStreamSource, long contentLength);

    /**
     * object key에 해당하는 파일을 다운로드합니다.
     *
     * @param objectKey 조회할 객체 키
     * @param byteRange HTTP Range 헤더 값. 전체 조회 시 null
     * @return 파일 스트림과 메타데이터
     */
    StoredFile download(String objectKey, String byteRange);

    /**
     * object key에 해당하는 파일을 삭제합니다.
     *
     * @param objectKey 삭제할 객체 키
     */
    void delete(String objectKey);
}
