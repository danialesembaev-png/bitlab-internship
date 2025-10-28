package com.example.demo.controller;

import com.example.demo.dto.CourseRequestDto;
import com.example.demo.dto.CourseResponseDto;
import com.example.demo.service.CourseService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private CourseResponseDto courseResponseDto;

    @BeforeEach
    void setUp() {
        courseResponseDto = new CourseResponseDto();
        courseResponseDto.setName("Java");
        courseResponseDto.setDescription("Backend course");
    }

    @Test
    void testGetAllCourses() throws Exception {
        Mockito.when(courseService.getAllCourses()).thenReturn(List.of(courseResponseDto));

        mockMvc.perform(get("/course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java"))
                .andExpect(jsonPath("$[0].description").value("Backend course"));
    }

    @Test
    void testGetCourseById() throws Exception {
        Mockito.when(courseService.getCourseById(1L)).thenReturn(courseResponseDto);

        mockMvc.perform(get("/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"))
                .andExpect(jsonPath("$.description").value("Backend course"));
    }

    @Test
    void testCreateCourse() throws Exception {
        Mockito.when(courseService.createCourse(any(CourseRequestDto.class))).thenReturn(courseResponseDto);

        mockMvc.perform(post("/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Java\",\"description\":\"Backend course\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"))
                .andExpect(jsonPath("$.description").value("Backend course"));
    }

    @Test
    void testUpdateCourse() throws Exception {
        Mockito.when(courseService.updateCourse(eq(1L), any(CourseRequestDto.class)))
                .thenReturn(courseResponseDto);

        mockMvc.perform(put("/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Java\",\"description\":\"Backend course\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"))
                .andExpect(jsonPath("$.description").value("Backend course"));
    }

    @Test
    void testDeleteCourse() throws Exception {
        mockMvc.perform(delete("/course/1"))
                .andExpect(status().isOk());

        Mockito.verify(courseService).deleteCourse(1L);
    }
}
