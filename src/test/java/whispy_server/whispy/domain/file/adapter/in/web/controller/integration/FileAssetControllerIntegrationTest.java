package whispy_server.whispy.domain.file.adapter.in.web.controller.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import whispy_server.whispy.domain.file.application.port.out.FileStoragePort;
import whispy_server.whispy.global.support.IntegrationTestSupport;

import java.io.ByteArrayInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("공개 파일 조회 통합 테스트")
class FileAssetControllerIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileStoragePort fileStoragePort;

    @Test
    @DisplayName("인증 없이 공개 파일 URL로 파일을 조회할 수 있다")
    void getFile_returnsStoredFileWithoutAuthentication() throws Exception {
        byte[] content = "music-data".getBytes();
        fileStoragePort.upload(
                "music_folder/sample.mp3",
                "audio/mpeg",
                () -> new ByteArrayInputStream(content),
                content.length
        );

        mockMvc.perform(get("/file/music_folder/sample.mp3"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "audio/mpeg"))
                .andExpect(header().longValue("Content-Length", content.length))
                .andExpect(content().bytes(content));
    }

    @Test
    @DisplayName("range 요청이 오면 206 partial content와 부분 바이트를 반환한다")
    void getFile_returnsPartialContent_whenRangeHeaderIsPresent() throws Exception {
        byte[] content = "music-data".getBytes();
        fileStoragePort.upload(
                "music_folder/sample.mp3",
                "audio/mpeg",
                () -> new ByteArrayInputStream(content),
                content.length
        );

        mockMvc.perform(get("/file/music_folder/sample.mp3").header(HttpHeaders.RANGE, "bytes=0-4"))
                .andExpect(status().isPartialContent())
                .andExpect(header().string(HttpHeaders.ACCEPT_RANGES, "bytes"))
                .andExpect(header().string(HttpHeaders.CONTENT_RANGE, "bytes 0-4/10"))
                .andExpect(header().longValue("Content-Length", 5))
                .andExpect(content().bytes("music".getBytes()));
    }

    @Test
    @DisplayName("multi-range 요청이면 전체 파일 응답으로 fallback한다")
    void getFile_returnsFullContent_whenMultiRangeHeaderIsPresent() throws Exception {
        byte[] content = "music-data".getBytes();
        fileStoragePort.upload(
                "music_folder/sample.mp3",
                "audio/mpeg",
                () -> new ByteArrayInputStream(content),
                content.length
        );

        mockMvc.perform(get("/file/music_folder/sample.mp3").header(HttpHeaders.RANGE, "bytes=0-1,4-5"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCEPT_RANGES, "bytes"))
                .andExpect(header().doesNotExist(HttpHeaders.CONTENT_RANGE))
                .andExpect(header().longValue("Content-Length", content.length))
                .andExpect(content().bytes(content));
    }

    @Test
    @DisplayName("malformed range 요청이면 416을 반환한다")
    void getFile_returnsRangeNotSatisfiable_whenRangeHeaderIsMalformed() throws Exception {
        byte[] content = "music-data".getBytes();
        fileStoragePort.upload(
                "music_folder/sample.mp3",
                "audio/mpeg",
                () -> new ByteArrayInputStream(content),
                content.length
        );

        mockMvc.perform(get("/file/music_folder/sample.mp3").header(HttpHeaders.RANGE, "bytes=a-b"))
                .andExpect(status().isRequestedRangeNotSatisfiable())
                .andExpect(jsonPath("$.status").value(416));
    }

    @Test
    @DisplayName("인증 없이 누락된 공개 파일 조회 시 404를 반환한다")
    void getFile_returnsNotFoundWithoutAuthentication_whenFileDoesNotExist() throws Exception {
        mockMvc.perform(get("/file/music_folder/missing.mp3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("파일을 찾을 수 없습니다"));
    }
}
