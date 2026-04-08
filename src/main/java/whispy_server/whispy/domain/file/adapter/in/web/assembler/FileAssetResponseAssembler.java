package whispy_server.whispy.domain.file.adapter.in.web.assembler;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpRange;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import whispy_server.whispy.domain.file.application.port.in.FileReadUseCase;
import whispy_server.whispy.domain.file.application.port.out.StoredFile;
import whispy_server.whispy.domain.file.application.utils.ImageFolderPathResolver;
import whispy_server.whispy.global.exception.domain.file.FileRangeNotSatisfiableException;

import java.util.List;
/**
 * 공개 파일 조회 결과를 HTTP 응답으로 조립하는 웹 어댑터 컴포넌트.
 */
@Component
@RequiredArgsConstructor
public class FileAssetResponseAssembler {

    private final FileReadUseCase fileReadUseCase;

    /**
     * 공개 파일 경로를 기반으로 저장소에서 파일을 읽어 HTTP 응답으로 반환합니다.
     *
     * @param folder 공개 URL에 포함된 폴더 경로 문자열
     * @param fileName 조회할 파일명
     * @param rangeHeader 클라이언트가 요청한 HTTP Range 헤더
     * @return 파일 스트림과 메타데이터를 포함한 HTTP 응답
     */
    public ResponseEntity<InputStreamResource> toResponse(String folder, String fileName, String rangeHeader) {
        String normalizedRange = normalizeRange(rangeHeader);
        StoredFile storedFile = fileReadUseCase.readFile(
                ImageFolderPathResolver.fromPathName(folder),
                fileName,
                normalizedRange
        );

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (storedFile.contentType() != null && !storedFile.contentType().isBlank()) {
            mediaType = MediaType.parseMediaType(storedFile.contentType());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        if (storedFile.contentLength() >= 0) {
            headers.setContentLength(storedFile.contentLength());
        }
        if (storedFile.partialContent() && storedFile.contentRange() != null) {
            headers.set(HttpHeaders.CONTENT_RANGE, storedFile.contentRange());
        }

        return ResponseEntity.status(storedFile.partialContent() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                .headers(headers)
                .body(new InputStreamResource(storedFile.inputStream()));
    }

    private String normalizeRange(String rangeHeader) {
        if (rangeHeader == null || rangeHeader.isBlank()) {
            return null;
        }

        List<HttpRange> ranges;
        try {
            ranges = HttpRange.parseRanges(rangeHeader);
        } catch (IllegalArgumentException exception) {
            throw FileRangeNotSatisfiableException.EXCEPTION;
        }

        if (ranges.size() != 1) {
            throw FileRangeNotSatisfiableException.EXCEPTION;
        }

        return rangeHeader;
    }
}
