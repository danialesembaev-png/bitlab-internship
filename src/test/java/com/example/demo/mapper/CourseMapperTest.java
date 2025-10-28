package com.example.demo.mapper;

import com.example.demo.dto.CourseRequestDto;
import com.example.demo.dto.CourseResponseDto;
import com.example.demo.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CourseMapperTest {

    private CourseMapper courseMapper;

    @BeforeEach
    void setUp() {
        // MapStruct автоматически сгенерирует реализацию CourseMapperImpl
        courseMapper = new CourseMapperImpl();
    }

    @Test
    void testToEntity() {
        CourseRequestDto dto = new CourseRequestDto();
        dto.setName("Java Basics");
        dto.setDescription("Learn Java from scratch");

        Course course = courseMapper.toEntity(dto);

        assertThat(course).isNotNull();
        assertThat(course.getId()).isNull(); // проверяем, что id игнорируется
        assertThat(course.getName()).isEqualTo("Java Basics");
        assertThat(course.getDescription()).isEqualTo("Learn Java from scratch");
    }

    @Test
    void testToDto() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Spring Boot");
        course.setDescription("Learn Spring Boot");

        CourseResponseDto dto = courseMapper.toDto(course);

        assertThat(dto).isNotNull();
        
        assertThat(dto.getName()).isEqualTo("Spring Boot");
        assertThat(dto.getDescription()).isEqualTo("Learn Spring Boot");
    }
}
