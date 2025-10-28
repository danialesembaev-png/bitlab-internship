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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private CourseRepository repository;

    @Mock
    private CourseMapper mapper;

    @InjectMocks
    private CourseServiceImpl service;

    private Course course;
    private CourseRequestDto requestDto;
    private CourseResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        course = new Course();
        course.setName("Java");
        course.setDescription("Java basics");

        requestDto = new CourseRequestDto();
        requestDto.setName("Java");
        requestDto.setDescription("Java basics");

        responseDto = new CourseResponseDto();
        responseDto.setName("Java");
        responseDto.setDescription("Java basics");
    }

    @Test
    void createCourse_ShouldReturnResponseDto() {
        when(mapper.toEntity(requestDto)).thenReturn(course);
        when(repository.save(course)).thenReturn(course);
        when(mapper.toDto(course)).thenReturn(responseDto);

        CourseResponseDto result = service.createCourse(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Java");
        assertThat(result.getDescription()).isEqualTo("Java basics");

        verify(repository, times(1)).save(course);
    }

    @Test
    void getAllCourses_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(course));
        when(mapper.toDto(course)).thenReturn(responseDto);

        List<CourseResponseDto> result = service.getAllCourses();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Java");
    }

    @Test
    void getCourseById_ShouldReturnResponseDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(course));
        when(mapper.toDto(course)).thenReturn(responseDto);

        CourseResponseDto result = service.getCourseById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Java");
    }

    @Test
    void updateCourse_ShouldUpdateAndReturnResponseDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(course));
        when(repository.save(course)).thenReturn(course);
        when(mapper.toDto(course)).thenReturn(responseDto);

        CourseResponseDto result = service.updateCourse(1L, requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Java");
        verify(repository).save(course);
    }

    @Test
    void deleteCourse_ShouldReturnTrue_WhenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean result = service.deleteCourse(1L);

        assertThat(result).isTrue();
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteCourse_ShouldReturnFalse_WhenNotExists() {
        when(repository.existsById(1L)).thenReturn(false);

        boolean result = service.deleteCourse(1L);

        assertThat(result).isFalse();
        verify(repository, never()).deleteById(1L);
    }
}
