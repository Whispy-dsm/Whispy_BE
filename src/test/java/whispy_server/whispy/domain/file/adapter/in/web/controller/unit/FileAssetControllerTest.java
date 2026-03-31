package whispy_server.whispy.domain.file.adapter.in.web.controller.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import whispy_server.whispy.domain.file.adapter.in.web.assembler.FileAssetResponseAssembler;
import whispy_server.whispy.domain.file.adapter.in.web.controller.FileAssetController;

import java.io.ByteArrayInputStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * FileAssetController의 단위 테스트 클래스.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FileAssetController 테스트")
class FileAssetControllerTest {

    @Mock
    private FileAssetResponseAssembler fileAssetResponseAssembler;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FileAssetController(fileAssetResponseAssembler)).build();
    }

    @Test
    @DisplayName("공개 파일 요청을 assembler에 그대로 위임한다")
    void getFile_delegatesToAssembler() throws Exception {
        byte[] content = "music-data".getBytes();
        ResponseEntity<InputStreamResource> response = ResponseEntity.ok()
                .header("Cache-Control", "public, max-age=2592000")
                .header("Content-Type", "audio/mpeg")
                .contentLength(content.length)
                .body(new InputStreamResource(new ByteArrayInputStream(content)));
        given(fileAssetResponseAssembler.toResponse("music_folder", "sample.mp3")).willReturn(response);

        mockMvc.perform(get("/file/music_folder/sample.mp3"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "audio/mpeg"))
                .andExpect(header().longValue("Content-Length", content.length))
                .andExpect(content().bytes(content));

        verify(fileAssetResponseAssembler).toResponse("music_folder", "sample.mp3");
    }
}
