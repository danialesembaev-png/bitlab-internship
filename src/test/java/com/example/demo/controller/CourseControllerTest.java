package com.example.demo.controller;

import com.example.demo.dto.CourseRequestDto;
import com.example.demo.service.CourseService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCourse_ShouldReturn200() throws Exception {
        CourseRequestDto dto = new CourseRequestDto();
        dto.setName("Java Basics");
        dto.setDescription("Intro to Java");

        // мок сервис
        Mockito.when(courseService.createCourse(any(CourseRequestDto.class)))
                .thenAnswer(invocation -> {
                    CourseRequestDto request = invocation.getArgument(0);
                    return new com.example.demo.dto.CourseResponseDto(1L, request.getName(), request.getDescription());
                });

        mockMvc.perform(post("/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java Basics"))
                .andExpect(jsonPath("$.description").value("Intro to Java"));
    }

    @Test
    void getAllCourses_ShouldReturnList() throws Exception {
        Mockito.when(courseService.getAllCourses())
                .thenReturn(List.of(new com.example.demo.dto.CourseResponseDto(1L, "Java Basics", "Intro to Java")));

        mockMvc.perform(get("/course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Java Basics"))
                .andExpect(jsonPath("$[0].description").value("Intro to Java"));
    }

    @Test
    void updateCourse_ShouldReturnUpdatedCourse() throws Exception {
        CourseRequestDto dto = new CourseRequestDto();
        dto.setName("Java Advanced");
        dto.setDescription("Advanced concepts");

        Mockito.when(courseService.updateCourse(eq(1L), any(CourseRequestDto.class)))
                .thenReturn(new com.example.demo.dto.CourseResponseDto(1L, dto.getName(), dto.getDescription()));

        mockMvc.perform(put("/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java Advanced"))
                .andExpect(jsonPath("$.description").value("Advanced concepts"));
    }

    @Test
    void deleteCourse_ShouldReturn200() throws Exception {
        mockMvc.perform(delete("/course/1"))
                .andExpect(status().isOk());

        Mockito.verify(courseService).deleteCourse(1L);
    }
}
