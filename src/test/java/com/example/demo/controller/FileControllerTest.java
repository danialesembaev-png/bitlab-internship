package com.example.demo.controller;

import com.example.demo.dto.FileDto;
import com.example.demo.service.impl.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)
@AutoConfigureMockMvc(addFilters = false)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    private MockMultipartFile testFile;

    @BeforeEach
    void setup() {
        testFile = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello World".getBytes()
        );
    }

    @Test
    void uploadFile_ShouldReturnFileName() throws Exception {
        Mockito.when(fileService.uploadFile(any(), any())).thenReturn("test.txt");

        mockMvc.perform(multipart("/file/upload")
                        .file(testFile))
                .andExpect(status().isOk())
                .andExpect(content().string("test.txt"));
    }

    @Test
    void downloadFile_ShouldReturnFileContent() throws Exception {
        ByteArrayResource resource = new ByteArrayResource("Hello World".getBytes());
        Mockito.when(fileService.downloadFile(eq("test.txt"))).thenReturn(resource);

        mockMvc.perform(get("/file/download/test.txt"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.txt\""))
                .andExpect(content().bytes("Hello World".getBytes()));
    }

    @Test
    void fileList_ShouldReturnListOfFiles() throws Exception {
        List<FileDto> files = List.of(
                FileDto.builder().fileName("file1.txt").build(),
                FileDto.builder().fileName("file2.txt").build()
        );

        Mockito.when(fileService.getFileList()).thenReturn(files);

        mockMvc.perform(get("/file/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].fileName").value("file1.txt"))
                .andExpect(jsonPath("$[1].fileName").value("file2.txt"));
    }
}
