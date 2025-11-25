package com.example.demo.controller;

import com.example.demo.dto.LessonRequestDto;
import com.example.demo.dto.LessonResponseDto;
import com.example.demo.service.LessonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
@AutoConfigureMockMvc(addFilters = false)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createLesson_ShouldReturn200() throws Exception {
        LessonRequestDto dto = new LessonRequestDto();
        dto.setTitle("Lesson 1");
        dto.setDescription("Intro");

        LessonResponseDto responseDto = new LessonResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle(dto.getTitle());
        responseDto.setDescription(dto.getDescription());

        Mockito.when(lessonService.createLesson(Mockito.any())).thenReturn(responseDto);

        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Lesson 1"));
    }

    @Test
    void getAllLessons_ShouldReturnList() throws Exception {
        LessonResponseDto l1 = new LessonResponseDto();
        l1.setId(1L);
        l1.setTitle("Lesson 1");

        LessonResponseDto l2 = new LessonResponseDto();
        l2.setId(2L);
        l2.setTitle("Lesson 2");

        List<LessonResponseDto> lessons = Arrays.asList(l1, l2);
        Mockito.when(lessonService.getAllLessons()).thenReturn(lessons);

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void updateLesson_ShouldReturnUpdatedLesson() throws Exception {
        LessonRequestDto dto = new LessonRequestDto();
        dto.setTitle("Updated Lesson");

        LessonResponseDto responseDto = new LessonResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle(dto.getTitle());

        Mockito.when(lessonService.updateLesson(Mockito.eq(1L), Mockito.any())).thenReturn(responseDto);

        mockMvc.perform(put("/lessons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Lesson"));
    }

    @Test
    void deleteLesson_ShouldReturnTrue() throws Exception {
        Mockito.when(lessonService.deleteLesson(1L)).thenReturn(true);

        mockMvc.perform(delete("/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
