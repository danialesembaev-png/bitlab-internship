package com.example.demo.mapper;

import com.example.demo.dto.LessonRequestDto;
import com.example.demo.dto.LessonResponseDto;
import com.example.demo.model.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class LessonMapperTest {

    private LessonMapper lessonMapper;

    @BeforeEach
    void setUp() {
        lessonMapper = Mappers.getMapper(LessonMapper.class);
    }

    @Test
    void testToEntity() {
        LessonRequestDto requestDto = new LessonRequestDto();
        requestDto.setTitle("Lesson 1");
        requestDto.setDescription("Description of Lesson 1");

        Lesson lesson = lessonMapper.toEntity(requestDto);

        assertNotNull(lesson);
        assertEquals("Lesson 1", lesson.getTitle());
        assertEquals("Description of Lesson 1", lesson.getDescription());
        assertNull(lesson.getId());
    }

    @Test
    void testToDto() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setTitle("Lesson 1");
        lesson.setDescription("Description of Lesson 1");

        LessonResponseDto dto = lessonMapper.toDto(lesson);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Lesson 1", dto.getTitle());
        assertEquals("Description of Lesson 1", dto.getDescription());
    }
}
