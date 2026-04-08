package whispy_server.whispy.domain.file.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whispy_server.whispy.domain.file.adapter.in.web.assembler.FileAssetResponseAssembler;
import whispy_server.whispy.global.document.api.file.FileAssetApiDocument;

/**
 * 공개 파일 URL(`/file/**`) 요청을 응답 조립기로 위임하는 컨트롤러.
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileAssetController implements FileAssetApiDocument {

    private final FileAssetResponseAssembler fileAssetResponseAssembler;

    /**
     * 공개 파일 조회 요청을 처리합니다.
     *
     * @param folder 공개 URL에 포함된 폴더 경로 문자열
     * @param fileName 조회할 파일명
     * @return 파일 스트림과 메타데이터를 포함한 HTTP 응답
     */
    @GetMapping("/{folder}/{fileName:.+}")
    public ResponseEntity<InputStreamResource> getFile(
            @PathVariable String folder,
            @PathVariable String fileName,
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String rangeHeader
    ) {
        return fileAssetResponseAssembler.toResponse(folder, fileName, rangeHeader);
    }
}
