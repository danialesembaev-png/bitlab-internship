package com.example.demo.controller;

import com.example.demo.dto.ChapterRequestDto;
import com.example.demo.dto.ChapterResponseDto;
import com.example.demo.service.ChapterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChapterController.class)
@AutoConfigureMockMvc(addFilters = false)  // ОТКЛЮЧАЕМ SECURITY
public class ChapterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChapterService chapterService;

    @Test
    void createChapter_ShouldReturn200() throws Exception {
        ChapterRequestDto request = new ChapterRequestDto();
        request.setName("Chapter 1");
        request.setDescription("Intro");
        request.setChapterOrder(1);

        ChapterResponseDto response = new ChapterResponseDto();
        response.setId(1L);
        response.setName("Chapter 1");
        response.setDescription("Intro");
        response.setChapterOrder(1);

        Mockito.when(chapterService.createChapter(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/chapters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Chapter 1"))
                .andExpect(jsonPath("$.description").value("Intro"));
    }

    @Test
    void getAllChapters_ShouldReturnList() throws Exception {
        ChapterResponseDto item = new ChapterResponseDto();
        item.setId(1L);
        item.setName("Chapter 1");
        item.setDescription("Intro");
        item.setChapterOrder(1);

        Mockito.when(chapterService.getAllChapters()).thenReturn(List.of(item));

        mockMvc.perform(get("/chapters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Chapter 1"))
                .andExpect(jsonPath("$[0].description").value("Intro"));
    }

    @Test
    void updateChapter_ShouldReturnUpdatedChapter() throws Exception {
        ChapterRequestDto request = new ChapterRequestDto();
        request.setName("Chapter 1");
        request.setDescription("Intro");
        request.setChapterOrder(1);

        ChapterResponseDto updated = new ChapterResponseDto();
        updated.setId(1L);
        updated.setName("Chapter 1");
        updated.setDescription("Intro");
        updated.setChapterOrder(1);

        Mockito.when(chapterService.updateChapter(Mockito.eq(1L), Mockito.any())).thenReturn(updated);

        mockMvc.perform(put("/chapters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Chapter 1"))
                .andExpect(jsonPath("$.description").value("Intro"));
    }
}
