package com.example.demo.controller;

import com.example.demo.dto.ChapterRequestDto;
import com.example.demo.dto.ChapterResponseDto;
import com.example.demo.model.Course;
import com.example.demo.service.ChapterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChapterController.class)
class ChapterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChapterService chapterService;

    private ChapterResponseDto chapterResponse;

    @BeforeEach
    void setUp() {
        Course course = new Course();
        course.setName("Java Backend");
        course.setDescription("Advanced backend course");

        chapterResponse = new ChapterResponseDto();
        chapterResponse.setName("Introduction to Spring");
        chapterResponse.setDescription("Learn the basics of Spring Boot");
        chapterResponse.setChapterOrder(1);
        chapterResponse.setCourse(course);
        chapterResponse.setCreatedTime(LocalDateTime.now());
        chapterResponse.setUpdatedTime(LocalDateTime.now());
    }

    @Test
    void testGetAllChapters() throws Exception {
        Mockito.when(chapterService.getAllChapters()).thenReturn(List.of(chapterResponse));

        mockMvc.perform(get("/chapters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Introduction to Spring"))
                .andExpect(jsonPath("$[0].description").value("Learn the basics of Spring Boot"));
    }

    @Test
    void testGetChapterById() throws Exception {
        Mockito.when(chapterService.getChapterById(1L)).thenReturn(chapterResponse);

        mockMvc.perform(get("/chapters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Introduction to Spring"))
                .andExpect(jsonPath("$.description").value("Learn the basics of Spring Boot"));
    }

    @Test
    void testCreateChapter() throws Exception {
        Mockito.when(chapterService.createChapter(any(ChapterRequestDto.class))).thenReturn(chapterResponse);

        mockMvc.perform(post("/chapters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Introduction to Spring\",\"description\":\"Learn the basics of Spring Boot\",\"courseId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Introduction to Spring"))
                .andExpect(jsonPath("$.description").value("Learn the basics of Spring Boot"));
    }

    @Test
    void testUpdateChapter() throws Exception {
        Mockito.when(chapterService.updateChapter(eq(1L), any(ChapterRequestDto.class)))
                .thenReturn(chapterResponse);

        mockMvc.perform(put("/chapters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Chapter\",\"description\":\"Updated description\",\"courseId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Introduction to Spring"))
                .andExpect(jsonPath("$.description").value("Learn the basics of Spring Boot"));
    }

    @Test
    void testDeleteChapter() throws Exception {
        Mockito.when(chapterService.deleteChapter(1L)).thenReturn(true);

        mockMvc.perform(delete("/chapters/1"))
                .andExpect(status().isOk());

        Mockito.verify(chapterService).deleteChapter(1L);
    }
}
