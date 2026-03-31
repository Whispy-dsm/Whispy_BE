package whispy_server.whispy.domain.file.adapter.out.external;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import whispy_server.whispy.global.exception.domain.file.FileNotFoundException;
import whispy_server.whispy.global.file.R2Properties;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("R2FileStorageAdapter 테스트")
class R2FileStorageAdapterTest {

    @InjectMocks
    private R2FileStorageAdapter adapter;

    @Mock
    private S3Client s3Client;

    @Mock
    private R2Properties r2Properties;

    @Test
    @DisplayName("삭제 대상 파일이 없으면 FileNotFoundException을 던진다")
    void delete_throwsFileNotFoundException_whenObjectDoesNotExist() {
        given(r2Properties.bucket()).willReturn("test-bucket");
        given(s3Client.headObject(any(HeadObjectRequest.class)))
                .willThrow(NoSuchKeyException.builder().message("missing").build());

        assertThatThrownBy(() -> adapter.delete("music_folder/missing.mp3"))
                .isSameAs(FileNotFoundException.EXCEPTION);
    }

    @Test
    @DisplayName("삭제 대상 파일이 있으면 head 확인 후 삭제한다")
    void delete_deletesObject_whenObjectExists() {
        given(r2Properties.bucket()).willReturn("test-bucket");
        given(s3Client.headObject(any(HeadObjectRequest.class)))
                .willReturn(HeadObjectResponse.builder().build());

        adapter.delete("music_folder/sample.mp3");

        verify(s3Client).headObject(any(HeadObjectRequest.class));
        verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
    }
}
