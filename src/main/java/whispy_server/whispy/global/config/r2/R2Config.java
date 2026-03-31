package whispy_server.whispy.global.config.r2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import whispy_server.whispy.global.file.R2Properties;

import java.net.URI;

/**
 * Cloudflare R2용 S3Client 설정.
 */
@Configuration
@Profile("!test")
public class R2Config {

    /**
     * Cloudflare R2와 통신할 AWS SDK v2 S3Client 빈을 생성합니다.
     *
     * @param r2Properties R2 접속 정보 프로퍼티
     * @return R2용 S3Client
     */
    @Bean
    public S3Client s3Client(R2Properties r2Properties) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                r2Properties.accessKeyId(),
                r2Properties.secretAccessKey()
        );

        S3Configuration serviceConfiguration = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .chunkedEncodingEnabled(false)
                .build();

        return S3Client.builder()
                .endpointOverride(URI.create(r2Properties.endpoint()))
                .region(Region.of("auto"))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(serviceConfiguration)
                .build();
    }
}
