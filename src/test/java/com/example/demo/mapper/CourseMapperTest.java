package com.example.demo.mapper;

import com.example.demo.dto.CourseRequestDto;
import com.example.demo.dto.CourseResponseDto;
import com.example.demo.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CourseMapperTest {

    private CourseMapper courseMapper;

    @BeforeEach
    void setUp() {
        courseMapper = Mappers.getMapper(CourseMapper.class);
    }

    @Test
    void testToEntity() {
        CourseRequestDto requestDto = new CourseRequestDto();
        requestDto.setName("Java Basics");
        requestDto.setDescription("Introduction to Java");

        Course course = courseMapper.toEntity(requestDto);

        assertNotNull(course);
        assertEquals("Java Basics", course.getName());
        assertEquals("Introduction to Java", course.getDescription());
        assertNull(course.getId());
    }

    @Test
    void testToDto() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Java Basics");
        course.setDescription("Introduction to Java");

        CourseResponseDto dto = courseMapper.toDto(course);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Java Basics", dto.getName());
        assertEquals("Introduction to Java", dto.getDescription());
    }
}
