package whispy_server.whispy.domain.file.application.service.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import whispy_server.whispy.domain.file.adapter.in.web.dto.FileUploadResponse;
import whispy_server.whispy.domain.file.application.service.FileUploadService;
import whispy_server.whispy.domain.file.application.utils.ImageCompressionConverter;
import whispy_server.whispy.domain.file.type.ImageFolder;
import whispy_server.whispy.global.exception.domain.file.FileUploadFailedException;
import whispy_server.whispy.global.file.FileProperties;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * FileUploadService의 단위 테스트 클래스
 *
 * 파일 업로드 서비스를 검증합니다.
 * 이미지 압축, 음악 파일 저장, URL 생성 로직을 테스트합니다.
 *
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

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("프로필 이미지를 업로드하고 URL을 반환한다")
    void uploadFile_uploadsProfileImage() throws IOException {
        // given
        byte[] imageContent = "test image content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                imageContent
        );

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());
        given(fileProperties.baseUrl()).willReturn("https://example.com");
        given(imageCompressionConverter.compressImage(any(MultipartFile.class)))
                .willReturn(new ByteArrayInputStream(imageContent));

        // when
        FileUploadResponse response = service.uploadFile(file, ImageFolder.PROFILE_IMAGE_FOLDER);

        // then
        assertThat(response.fileUrl()).startsWith("https://example.com/file/profile_image_folder/");
        assertThat(response.fileUrl()).endsWith(".webp");

        // 파일이 실제로 저장되었는지 확인
        Path folderPath = tempDir.resolve("profile_image_folder");
        assertTrue(Files.exists(folderPath));
        assertTrue(Files.list(folderPath).findAny().isPresent());
    }

    @Test
    @DisplayName("음악 배너 이미지를 업로드하고 URL을 반환한다")
    void uploadFile_uploadsMusicBannerImage() throws IOException {
        // given
        byte[] imageContent = "test banner content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "banner.png",
                "image/png",
                imageContent
        );

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());
        given(fileProperties.baseUrl()).willReturn("https://example.com");
        given(imageCompressionConverter.compressImage(any(MultipartFile.class)))
                .willReturn(new ByteArrayInputStream(imageContent));

        // when
        FileUploadResponse response = service.uploadFile(file, ImageFolder.MUSIC_BANNER_IMAGE_FOLDER);

        // then
        assertThat(response.fileUrl()).startsWith("https://example.com/file/music_banner_image_folder/");
        assertThat(response.fileUrl()).endsWith(".webp");

        Path folderPath = tempDir.resolve("music_banner_image_folder");
        assertTrue(Files.exists(folderPath));
    }

    @Test
    @DisplayName("음악 파일을 업로드하고 URL을 반환한다")
    void uploadFile_uploadsMusicFile() throws IOException {
        // given
        byte[] musicContent = "test music content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-song.mp3",
                "audio/mpeg",
                musicContent
        );

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());
        given(fileProperties.baseUrl()).willReturn("https://example.com");

        // when
        FileUploadResponse response = service.uploadFile(file, ImageFolder.MUSIC_FOLDER);

        // then
        assertThat(response.fileUrl()).startsWith("https://example.com/file/music_folder/");
        assertThat(response.fileUrl()).endsWith(".mp3");

        // 음악 파일은 압축하지 않고 원본 저장
        Path folderPath = tempDir.resolve("music_folder");
        assertTrue(Files.exists(folderPath));
        assertTrue(Files.list(folderPath).findAny().isPresent());
    }

    @Test
    @DisplayName("음악 파일은 원본 확장자를 유지한다")
    void uploadFile_preservesMusicFileExtension() throws IOException {
        // given
        byte[] musicContent = "test music content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "song.flac",
                "audio/flac",
                musicContent
        );

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());
        given(fileProperties.baseUrl()).willReturn("https://example.com");

        // when
        FileUploadResponse response = service.uploadFile(file, ImageFolder.MUSIC_FOLDER);

        // then
        assertThat(response.fileUrl()).endsWith(".flac");
    }

    @Test
    @DisplayName("이미지 파일은 webp 확장자로 변환된다")
    void uploadFile_convertsImageToWebp() throws IOException {
        // given
        byte[] imageContent = "test image".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.jpg",
                "image/jpeg",
                imageContent
        );

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());
        given(fileProperties.baseUrl()).willReturn("https://example.com");
        given(imageCompressionConverter.compressImage(any(MultipartFile.class)))
                .willReturn(new ByteArrayInputStream(imageContent));

        // when
        FileUploadResponse response = service.uploadFile(file, ImageFolder.PROFILE_IMAGE_FOLDER);

        // then
        assertThat(response.fileUrl()).endsWith(".webp");
    }

    @Test
    @DisplayName("파일 URL은 올바른 형식으로 생성된다")
    void uploadFile_generatesCorrectFileUrl() throws IOException {
        // given
        byte[] imageContent = "test".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                imageContent
        );

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());
        given(fileProperties.baseUrl()).willReturn("https://storage.example.com");
        given(imageCompressionConverter.compressImage(any(MultipartFile.class)))
                .willReturn(new ByteArrayInputStream(imageContent));

        // when
        FileUploadResponse response = service.uploadFile(file, ImageFolder.PROFILE_IMAGE_FOLDER);

        // then
        assertThat(response.fileUrl()).matches(
                "https://storage\\.example\\.com/file/profile_image_folder/[a-f0-9\\-]+\\.webp"
        );
    }

    @Test
    @DisplayName("폴더가 존재하지 않으면 자동으로 생성한다")
    void uploadFile_createsDirectoryIfNotExists() throws IOException {
        // given
        byte[] imageContent = "test".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                imageContent
        );

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());
        given(fileProperties.baseUrl()).willReturn("https://example.com");
        given(imageCompressionConverter.compressImage(any(MultipartFile.class)))
                .willReturn(new ByteArrayInputStream(imageContent));

        Path folderPath = tempDir.resolve("profile_image_folder");
        assertThat(folderPath).doesNotExist();

        // when
        service.uploadFile(file, ImageFolder.PROFILE_IMAGE_FOLDER);

        // then
        assertThat(folderPath).exists();
    }

    @Test
    @DisplayName("여러 종류의 이미지 확장자를 지원한다")
    void uploadFile_supportsVariousImageExtensions() throws IOException {
        // given
        byte[] imageContent = "test image".getBytes();
        MockMultipartFile pngFile = new MockMultipartFile(
                "file",
                "image.png",
                "image/png",
                imageContent
        );

        given(fileProperties.uploadPath()).willReturn(tempDir.toString());
        given(fileProperties.baseUrl()).willReturn("https://example.com");
        given(imageCompressionConverter.compressImage(any(MultipartFile.class)))
                .willReturn(new ByteArrayInputStream(imageContent));

        // when
        FileUploadResponse response = service.uploadFile(pngFile, ImageFolder.PROFILE_IMAGE_FOLDER);

        // then
        assertThat(response.fileUrl()).endsWith(".webp");
        Path folderPath = tempDir.resolve("profile_image_folder");
        assertTrue(Files.exists(folderPath));
    }
}
