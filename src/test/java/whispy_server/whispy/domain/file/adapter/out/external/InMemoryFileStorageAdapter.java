package whispy_server.whispy.domain.file.adapter.out.external;

import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpRange;
import whispy_server.whispy.domain.file.application.port.out.FileStoragePort;
import whispy_server.whispy.domain.file.application.port.out.StoredFile;
import whispy_server.whispy.global.exception.domain.file.FileNotFoundException;
import whispy_server.whispy.global.exception.domain.file.FileRangeNotSatisfiableException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 테스트 프로파일용 메모리 기반 파일 저장소 외부 어댑터.
 */
public class InMemoryFileStorageAdapter implements FileStoragePort {

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private final Map<String, InMemoryFile> storage = new ConcurrentHashMap<>();

    /**
     * 테스트 메모리 저장소에 파일을 업로드합니다.
     *
     * @param objectKey 저장할 객체 키
     * @param contentType 파일 MIME 타입
     * @param inputStreamSource 업로드 시마다 새 스트림을 제공하는 공급자
     * @param contentLength 파일 바이트 수
     */
    @Override
    public void upload(String objectKey, String contentType, InputStreamSource inputStreamSource, long contentLength) {
        try (InputStream stream = inputStreamSource.getInputStream()) {
            storage.put(objectKey, new InMemoryFile(stream.readAllBytes(), normalizeContentType(contentType)));
        } catch (IOException exception) {
            throw new IllegalStateException("메모리 파일 저장에 실패했습니다.", exception);
        }
    }

    /**
     * 테스트 메모리 저장소에서 파일을 조회합니다.
     *
     * @param objectKey 조회할 객체 키
     * @return 파일 스트림과 메타데이터
     */
    @Override
    public StoredFile download(String objectKey, String byteRange) {
        InMemoryFile file = storage.get(objectKey);
        if (file == null) {
            throw FileNotFoundException.EXCEPTION;
        }

        if (byteRange == null || byteRange.isBlank()) {
            return new StoredFile(new ByteArrayInputStream(file.content()), file.contentType(), file.content().length);
        }

        try {
            List<HttpRange> ranges = HttpRange.parseRanges(byteRange);
            if (ranges.size() != 1) {
                throw FileRangeNotSatisfiableException.EXCEPTION;
            }

            HttpRange range = ranges.get(0);
            long start = range.getRangeStart(file.content().length);
            long end = range.getRangeEnd(file.content().length);
            byte[] partialContent = Arrays.copyOfRange(file.content(), (int) start, (int) end + 1);
            String contentRange = "bytes %d-%d/%d".formatted(start, end, file.content().length);

            return new StoredFile(
                    new ByteArrayInputStream(partialContent),
                    file.contentType(),
                    partialContent.length,
                    true,
                    contentRange
            );
        } catch (IllegalArgumentException exception) {
            throw FileRangeNotSatisfiableException.EXCEPTION;
        }
    }

    /**
     * 테스트 메모리 저장소에서 파일을 삭제합니다.
     *
     * @param objectKey 삭제할 객체 키
     */
    @Override
    public void delete(String objectKey) {
        InMemoryFile removed = storage.remove(objectKey);
        if (removed == null) {
            throw FileNotFoundException.EXCEPTION;
        }
    }

    private String normalizeContentType(String contentType) {
        return contentType != null && !contentType.isBlank() ? contentType : DEFAULT_CONTENT_TYPE;
    }

    private record InMemoryFile(byte[] content, String contentType) {
    }
}
