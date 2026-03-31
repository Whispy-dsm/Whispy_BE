package whispy_server.whispy.domain.file.adapter.in.web.assembler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import whispy_server.whispy.domain.file.application.port.in.FileReadUseCase;
import whispy_server.whispy.domain.file.application.port.out.StoredFile;
import whispy_server.whispy.domain.file.type.ImageFolder;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
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
        given(fileReadUseCase.readFile(ImageFolder.MUSIC_FOLDER, "sample.mp3")).willReturn(storedFile);

        ResponseEntity<InputStreamResource> response = assembler.toResponse("music_folder", "sample.mp3");

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.valueOf("audio/mpeg"));
        assertThat(response.getHeaders().getContentLength()).isEqualTo(content.length);
        assertThat(response.getHeaders().getCacheControl()).contains("max-age");
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInputStream().readAllBytes()).isEqualTo(content);
    }

    @Test
    @DisplayName("content type이 없으면 octet-stream을 사용한다")
    void toResponse_usesOctetStream_whenContentTypeIsBlank() {
        byte[] content = "binary-data".getBytes();
        StoredFile storedFile = new StoredFile(new ByteArrayInputStream(content), " ", -1L);
        given(fileReadUseCase.readFile(ImageFolder.PROFILE_IMAGE_FOLDER, "sample.bin")).willReturn(storedFile);

        ResponseEntity<InputStreamResource> response = assembler.toResponse("profile_image_folder", "sample.bin");

        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_OCTET_STREAM);
        assertThat(response.getHeaders().getContentLength()).isEqualTo(-1L);
    }
}
