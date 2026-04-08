package whispy_server.whispy.domain.file.adapter.in.web.assembler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import whispy_server.whispy.domain.file.application.port.in.FileReadUseCase;
import whispy_server.whispy.domain.file.application.port.out.StoredFile;
import whispy_server.whispy.domain.file.type.ImageFolder;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("FileAssetResponseAssembler 테스트")
class FileAssetResponseAssemblerTest {

    @InjectMocks
    private FileAssetResponseAssembler assembler;

    @Mock
    private FileReadUseCase fileReadUseCase;

    @Test
    @DisplayName("stored file을 공개 파일 응답으로 조립할 수 있다")
    void toResponse_buildsPublicFileResponse() throws IOException {
        byte[] content = "music-data".getBytes();
        StoredFile storedFile = new StoredFile(new ByteArrayInputStream(content), "audio/mpeg", content.length);
        given(fileReadUseCase.readFile(ImageFolder.MUSIC_FOLDER, "sample.mp3", null)).willReturn(storedFile);

        ResponseEntity<InputStreamResource> response = assembler.toResponse("music_folder", "sample.mp3", null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.valueOf("audio/mpeg"));
        assertThat(response.getHeaders().getContentLength()).isEqualTo(content.length);
        assertThat(response.getHeaders().getFirst(HttpHeaders.ACCEPT_RANGES)).isEqualTo("bytes");
        assertThat(response.getHeaders().containsKey(HttpHeaders.CACHE_CONTROL)).isFalse();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInputStream().readAllBytes()).isEqualTo(content);
    }

    @Test
    @DisplayName("content type이 없으면 octet-stream을 사용한다")
    void toResponse_usesOctetStream_whenContentTypeIsBlank() {
        byte[] content = "binary-data".getBytes();
        StoredFile storedFile = new StoredFile(new ByteArrayInputStream(content), " ", -1L);
        given(fileReadUseCase.readFile(ImageFolder.PROFILE_IMAGE_FOLDER, "sample.bin", null)).willReturn(storedFile);

        ResponseEntity<InputStreamResource> response = assembler.toResponse("profile_image_folder", "sample.bin", null);

        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_OCTET_STREAM);
        assertThat(response.getHeaders().getContentLength()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("range header가 있으면 206 partial content로 응답한다")
    void toResponse_returnsPartialContent_whenRangeHeaderIsPresent() throws IOException {
        byte[] content = "music".getBytes();
        StoredFile storedFile = new StoredFile(
                new ByteArrayInputStream(content),
                "audio/mpeg",
                content.length,
                true,
                "bytes 0-4/10"
        );
        given(fileReadUseCase.readFile(ImageFolder.MUSIC_FOLDER, "sample.mp3", "bytes=0-4")).willReturn(storedFile);

        ResponseEntity<InputStreamResource> response = assembler.toResponse("music_folder", "sample.mp3", "bytes=0-4");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PARTIAL_CONTENT);
        assertThat(response.getHeaders().getFirst(HttpHeaders.ACCEPT_RANGES)).isEqualTo("bytes");
        assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_RANGE)).isEqualTo("bytes 0-4/10");
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInputStream().readAllBytes()).isEqualTo(content);
    }

    @Test
    @DisplayName("malformed range header면 416 예외를 던진다")
    void toResponse_throwsRangeNotSatisfiable_whenRangeHeaderIsMalformed() {
        assertThatThrownBy(() -> assembler.toResponse("music_folder", "sample.mp3", "bytes=a-b"))
                .isSameAs(whispy_server.whispy.global.exception.domain.file.FileRangeNotSatisfiableException.EXCEPTION);
    }
}
