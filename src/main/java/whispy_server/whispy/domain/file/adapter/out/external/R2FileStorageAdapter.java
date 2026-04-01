package whispy_server.whispy.domain.file.adapter.out.external;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.ContentStreamProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import whispy_server.whispy.domain.file.application.port.out.FileStoragePort;
import whispy_server.whispy.domain.file.application.port.out.StoredFile;
import whispy_server.whispy.global.exception.domain.file.FileDeleteFailedException;
import whispy_server.whispy.global.exception.domain.file.FileNotFoundException;
import whispy_server.whispy.global.exception.domain.file.FileReadFailedException;
import whispy_server.whispy.global.exception.domain.file.FileUploadFailedException;
import whispy_server.whispy.global.file.R2Properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * Cloudflare R2 기반 파일 저장소 외부 어댑터.
 */
@Component
@ConditionalOnBean(S3Client.class)
@RequiredArgsConstructor
public class R2FileStorageAdapter implements FileStoragePort {

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private final S3Client s3Client;
    private final R2Properties r2Properties;

    /**
     * Cloudflare R2에 파일을 업로드합니다.
     *
     * @param objectKey 저장소 내부에서 사용할 객체 키
     * @param contentType 저장할 파일의 MIME 타입
     * @param inputStreamSource 업로드 시마다 새 스트림을 제공하는 공급자
     * @param contentLength 업로드할 파일 바이트 수
     */
    @Override
    public void upload(String objectKey, String contentType, InputStreamSource inputStreamSource, long contentLength) {
        String resolvedContentType = contentType != null && !contentType.isBlank() ? contentType : DEFAULT_CONTENT_TYPE;
        ContentStreamProvider contentStreamProvider = () -> {
            try {
                return inputStreamSource.getInputStream();
            } catch (IOException exception) {
                throw new UncheckedIOException(exception);
            }
        };

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(r2Properties.bucket())
                            .key(objectKey)
                            .contentType(resolvedContentType)
                            .build(),
                    RequestBody.fromContentProvider(contentStreamProvider, contentLength, resolvedContentType)
            );
        } catch (UncheckedIOException exception) {
            throw new FileUploadFailedException(exception.getCause());
        } catch (Exception e) {
            throw new FileUploadFailedException(e);
        }
    }

    /**
     * Cloudflare R2에서 object key에 해당하는 파일을 조회합니다.
     *
     * @param objectKey 조회할 객체 키
     * @return 파일 스트림과 메타데이터
     */
    @Override
    public StoredFile download(String objectKey) {
        try {
            ResponseInputStream<GetObjectResponse> objectStream = s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(r2Properties.bucket())
                            .key(objectKey)
                            .build()
            );

            GetObjectResponse response = objectStream.response();
            String contentType = response.contentType() != null ? response.contentType() : DEFAULT_CONTENT_TYPE;
            long contentLength = response.contentLength() != null ? response.contentLength() : -1L;
            return new StoredFile(objectStream, contentType, contentLength);

        } catch (NoSuchKeyException e) {
            throw FileNotFoundException.EXCEPTION;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                throw FileNotFoundException.EXCEPTION;
            }
            throw new FileReadFailedException(e);
        } catch (RuntimeException e) {
            throw new FileReadFailedException(e);
        }
    }

    /**
     * Cloudflare R2에서 object key에 해당하는 파일을 삭제합니다.
     *
     * @param objectKey 삭제할 객체 키
     */
    @Override
    public void delete(String objectKey) {
        try {
            s3Client.headObject(
                    HeadObjectRequest.builder()
                            .bucket(r2Properties.bucket())
                            .key(objectKey)
                            .build()
            );

            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(r2Properties.bucket())
                            .key(objectKey)
                            .build()
            );

        } catch (NoSuchKeyException e) {
            throw FileNotFoundException.EXCEPTION;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                throw FileNotFoundException.EXCEPTION;
            }
            throw new FileDeleteFailedException(e);
        } catch (RuntimeException e) {
            throw new FileDeleteFailedException(e);
        }
    }
}
