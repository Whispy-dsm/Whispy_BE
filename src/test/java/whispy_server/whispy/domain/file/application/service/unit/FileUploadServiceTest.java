package whispy_server.whispy.domain.file.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.domain.file.adapter.in.web.dto.response.FileUploadResponse;
import whispy_server.whispy.domain.file.application.port.out.FileStoragePort;
import whispy_server.whispy.domain.file.application.service.FileUploadService;
import whispy_server.whispy.domain.file.application.utils.ImageFolderPathResolver;
import whispy_server.whispy.domain.file.application.utils.ImageCompressionConverter;
import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.file.FileProperties;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * FileUploadService의 단위 테스트 클래스.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FileUploadService 테스트")
class FileUploadServiceTest {

    @InjectMocks
    private FileUploadService service;

    @Mock
    private FileProperties fileProperties;

    @Mock
    private ImageCompressionConverter imageCompressionConverter;

    @Mock
    private FileStoragePort fileStoragePort;

    @Test
    @DisplayName("프로필 이미지를 업로드하고 기존 URL 형식을 반환한다")
    void uploadFile_uploadsProfileImage() throws IOException {
        byte[] imageContent = "test image content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                imageContent
        );

        given(fileProperties.baseUrl()).willReturn("https://example.com");
        given(imageCompressionConverter.compressImage(any(MultipartFile.class)))
                .willReturn(new ByteArrayInputStream(imageContent));

        FileUploadResponse response = service.uploadFile(file, ImageFolder.PROFILE_IMAGE_FOLDER);

        assertThat(response.fileUrl()).startsWith("https://example.com/file/profile_image_folder/");
        assertThat(response.fileUrl()).endsWith(".webp");

        verify(fileStoragePort).upload(
                eq(ImageFolderPathResolver.toObjectKey(ImageFolder.PROFILE_IMAGE_FOLDER, extractFileName(response.fileUrl()))),
                eq("image/webp"),
                any(InputStreamSource.class),
                eq((long) imageContent.length)
        );
    }

    @Test
    @DisplayName("공지 배너 이미지를 업로드할 수 있다")
    void uploadFile_uploadsAnnouncementBannerImage() throws IOException {
        byte[] imageContent = "announcement banner".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "banner.png",
                "image/png",
                imageContent
        );

        given(fileProperties.baseUrl()).willReturn("https://example.com");
        given(imageCompressionConverter.compressImage(any(MultipartFile.class)))
                .willReturn(new ByteArrayInputStream(imageContent));

        FileUploadResponse response = service.uploadFile(file, ImageFolder.ANNOUNCEMENT_BANNER_IMAGE_FOLDER);

        assertThat(response.fileUrl()).startsWith("https://example.com/file/announcement_banner_image_folder/");
        assertThat(response.fileUrl()).endsWith(".webp");

        verify(fileStoragePort).upload(
                eq(ImageFolderPathResolver.toObjectKey(ImageFolder.ANNOUNCEMENT_BANNER_IMAGE_FOLDER, extractFileName(response.fileUrl()))),
                eq("image/webp"),
                any(InputStreamSource.class),
                eq((long) imageContent.length)
        );
    }

    @Test
    @DisplayName("음악 파일을 업로드하고 원본 확장자를 유지한다")
    void uploadFile_uploadsMusicFile() throws IOException {
        byte[] musicContent = "test music content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-song.mp3",
                "audio/mpeg",
                musicContent
        );

        given(fileProperties.baseUrl()).willReturn("https://example.com");

        FileUploadResponse response = service.uploadFile(file, ImageFolder.MUSIC_FOLDER);

        assertThat(response.fileUrl()).startsWith("https://example.com/file/music_folder/");
        assertThat(response.fileUrl()).endsWith(".mp3");

        ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<InputStreamSource> streamSourceCaptor = ArgumentCaptor.forClass(InputStreamSource.class);
        verify(fileStoragePort).upload(
                fileNameCaptor.capture(),
                eq("audio/mpeg"),
                streamSourceCaptor.capture(),
                eq((long) musicContent.length)
        );
        assertThat(fileNameCaptor.getValue()).startsWith("music_folder/");
        assertThat(fileNameCaptor.getValue()).endsWith(".mp3");
        assertThat(streamSourceCaptor.getValue().getInputStream().readAllBytes()).isEqualTo(musicContent);
        verify(imageCompressionConverter, never()).compressImage(any(MultipartFile.class));
    }

    @Test
    @DisplayName("음악 파일은 원본 확장자를 유지한다")
    void uploadFile_preservesMusicFileExtension() {
        byte[] musicContent = "test music content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "song.flac",
                "audio/flac",
                musicContent
        );

        given(fileProperties.baseUrl()).willReturn("https://example.com");

        FileUploadResponse response = service.uploadFile(file, ImageFolder.MUSIC_FOLDER);

        assertThat(response.fileUrl()).endsWith(".flac");
    }

    @Test
    @DisplayName("이미지 파일은 webp 확장자로 변환된다")
    void uploadFile_convertsImageToWebp() throws IOException {
        byte[] imageContent = "test image".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.jpg",
                "image/jpeg",
                imageContent
        );

        given(fileProperties.baseUrl()).willReturn("https://example.com");
        given(imageCompressionConverter.compressImage(any(MultipartFile.class)))
                .willReturn(new ByteArrayInputStream(imageContent));

        FileUploadResponse response = service.uploadFile(file, ImageFolder.PROFILE_IMAGE_FOLDER);

        assertThat(response.fileUrl()).endsWith(".webp");
    }

    @Test
    @DisplayName("파일 URL은 올바른 형식으로 생성된다")
    void uploadFile_generatesCorrectFileUrl() throws IOException {
        byte[] imageContent = "test".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                imageContent
        );

        given(fileProperties.baseUrl()).willReturn("https://storage.example.com");
        given(imageCompressionConverter.compressImage(any(MultipartFile.class)))
                .willReturn(new ByteArrayInputStream(imageContent));

        FileUploadResponse response = service.uploadFile(file, ImageFolder.PROFILE_IMAGE_FOLDER);

        assertThat(response.fileUrl()).matches(
                "https://storage\\.example\\.com/file/profile_image_folder/[a-f0-9\\-]+\\.webp"
        );
    }

    @Test
    @DisplayName("webp 이미지는 재압축하지 않고 원본 그대로 업로드한다")
    void uploadFile_uploadsWebpWithoutCompression() {
        byte[] imageContent = "test image".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.webp",
                "image/webp",
                imageContent
        );

        given(fileProperties.baseUrl()).willReturn("https://example.com");

        FileUploadResponse response = service.uploadFile(file, ImageFolder.PROFILE_IMAGE_FOLDER);

        verify(fileStoragePort).upload(
                eq(ImageFolderPathResolver.toObjectKey(ImageFolder.PROFILE_IMAGE_FOLDER, extractFileName(response.fileUrl()))),
                eq("image/webp"),
                any(InputStreamSource.class),
                eq((long) imageContent.length)
        );
        verify(imageCompressionConverter, never()).compressImage(any(MultipartFile.class));
    }

    private String extractFileName(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
    }
}
