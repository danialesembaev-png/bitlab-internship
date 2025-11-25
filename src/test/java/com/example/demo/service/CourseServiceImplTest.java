package com.example.demo.service;

import com.example.demo.dto.CourseRequestDto;
import com.example.demo.dto.CourseResponseDto;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private CourseRepository repository;

    @Mock
    private CourseMapper mapper;

    @InjectMocks
    private CourseServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCourses() {
        Course course1 = new Course();
        Course course2 = new Course();

        CourseResponseDto dto1 = new CourseResponseDto();
        CourseResponseDto dto2 = new CourseResponseDto();

        when(repository.findAll()).thenReturn(List.of(course1, course2));
        when(mapper.toDto(course1)).thenReturn(dto1);
        when(mapper.toDto(course2)).thenReturn(dto2);

        List<CourseResponseDto> result = service.getAllCourses();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void testGetCourseById_Found() {
        Course course = new Course();
        CourseResponseDto dto = new CourseResponseDto();

        when(repository.findById(1L)).thenReturn(Optional.of(course));
        when(mapper.toDto(course)).thenReturn(dto);

        CourseResponseDto result = service.getCourseById(1L);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void testGetCourseById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        CourseResponseDto result = service.getCourseById(1L);

        assertNull(result);
    }

    @Test
    void testCreateCourse() {
        CourseRequestDto request = new CourseRequestDto();
        Course course = new Course();
        Course saved = new Course();
        CourseResponseDto dto = new CourseResponseDto();

        when(mapper.toEntity(request)).thenReturn(course);
        when(repository.save(course)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);

        CourseResponseDto result = service.createCourse(request);

        assertEquals(dto, result);
        verify(repository, times(1)).save(course);
    }

    @Test
    void testUpdateCourse_Found() {
        CourseRequestDto request = new CourseRequestDto();
        request.setName("New Name");
        request.setDescription("New Description");

        Course existing = new Course();
        Course updated = new Course();
        CourseResponseDto dto = new CourseResponseDto();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(updated);
        when(mapper.toDto(updated)).thenReturn(dto);

        CourseResponseDto result = service.updateCourse(1L, request);

        assertEquals(dto, result);
        assertEquals("New Name", existing.getName());
        assertEquals("New Description", existing.getDescription());
    }

    @Test
    void testUpdateCourse_NotFound() {
        CourseRequestDto request = new CourseRequestDto();
        when(repository.findById(1L)).thenReturn(Optional.empty());

        CourseResponseDto result = service.updateCourse(1L, request);

        assertNull(result);
    }

    @Test
    void testDeleteCourse_Exists() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean result = service.deleteCourse(1L);

        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCourse_NotExists() {
        when(repository.existsById(1L)).thenReturn(false);

        boolean result = service.deleteCourse(1L);

        assertFalse(result);
        verify(repository, never()).deleteById(anyLong());
    }
}
