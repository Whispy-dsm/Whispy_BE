package whispy_server.whispy.domain.file.application.port.out;

import java.io.InputStream;

/**
 * 저장소에서 읽어온 파일 스트림과 메타데이터를 담는 값 객체.
 *
 * @param inputStream 파일 본문 스트림
 * @param contentType 파일 MIME 타입
 * @param contentLength 파일 크기(알 수 없으면 음수)
 */
public record StoredFile(
        InputStream inputStream,
        String contentType,
        long contentLength,
        boolean partialContent,
        String contentRange
) {
    public StoredFile(InputStream inputStream, String contentType, long contentLength) {
        this(inputStream, contentType, contentLength, false, null);
    }
}
