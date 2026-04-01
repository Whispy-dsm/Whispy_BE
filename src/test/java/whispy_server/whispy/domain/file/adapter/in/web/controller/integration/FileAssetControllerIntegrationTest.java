package whispy_server.whispy.domain.file.adapter.in.web.controller.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    @DisplayName("인증 없이 누락된 공개 파일 조회 시 404를 반환한다")
    void getFile_returnsNotFoundWithoutAuthentication_whenFileDoesNotExist() throws Exception {
        mockMvc.perform(get("/file/music_folder/missing.mp3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("파일을 찾을 수 없습니다"));
    }
}
