package com.example.demo.controller;

import com.example.demo.dto.LessonRequestDto;
import com.example.demo.dto.LessonResponseDto;
import com.example.demo.service.LessonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LessonController.class)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @Autowired
    private ObjectMapper objectMapper;

    private LessonResponseDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new LessonResponseDto();
        sampleDto.setId(1L);
        sampleDto.setTitle("Spring Basics");
        sampleDto.setDescription("Intro to Spring");
    }

    @Test
    void createLesson_ShouldReturnCreatedLesson() throws Exception {
        LessonRequestDto req = new LessonRequestDto();
        req.setTitle("Spring Basics");
        req.setDescription("Intro to Spring");
        req.setChapterId(1L);

        Mockito.when(lessonService.createLesson(any(LessonRequestDto.class)))
                .thenReturn(sampleDto);

        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())                     // твой контроллер возвращал 200
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Spring Basics"))
                .andExpect(jsonPath("$.description").value("Intro to Spring"));
    }

    @Test
    void getAllLessons_ShouldReturnList() throws Exception {
        Mockito.when(lessonService.getAllLessons()).thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Spring Basics"));
    }

    @Test
    void getLessonById_ShouldReturnLesson() throws Exception {
        Mockito.when(lessonService.getLessonById(1L)).thenReturn(sampleDto);

        mockMvc.perform(get("/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Spring Basics"));
    }

    @Test
    void updateLesson_ShouldReturnUpdatedLesson() throws Exception {
        LessonRequestDto req = new LessonRequestDto();
        req.setTitle("Updated Title");

        LessonResponseDto updated = new LessonResponseDto();
        updated.setId(1L);
        updated.setTitle("Updated Title");
        updated.setDescription("Intro to Spring");

        Mockito.when(lessonService.updateLesson(eq(1L), any(LessonRequestDto.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/lessons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void deleteLesson_WhenExists_ShouldReturnTrue() throws Exception {
        Mockito.when(lessonService.deleteLesson(1L)).thenReturn(true);

        mockMvc.perform(delete("/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true")); // контроллер возвращает boolean -> тело "true"
    }

    @Test
    void deleteLesson_WhenNotExists_ShouldReturnFalse() throws Exception {
        Mockito.when(lessonService.deleteLesson(2L)).thenReturn(false);

        mockMvc.perform(delete("/lessons/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
