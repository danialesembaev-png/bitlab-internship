package com.example.demo.mapper;

import com.example.demo.dto.ChapterRequestDto;
import com.example.demo.dto.ChapterResponseDto;
import com.example.demo.model.Chapter;
import com.example.demo.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ChapterMapperTest {

    private ChapterMapper mapper;

    @BeforeEach
    void setUp() {
        // Берём сгенерированную реализацию маппера
        mapper = Mappers.getMapper(ChapterMapper.class);
    }

    @Test
    void testToEntity() {
        ChapterRequestDto dto = new ChapterRequestDto();
        dto.setName("Introduction");
        dto.setDescription("First chapter");
        dto.setChapterOrder(1);

        Chapter chapter = mapper.toEntity(dto);

        assertNotNull(chapter);
        assertNull(chapter.getId()); // потому что @Mapping(target="id", ignore=true)
        assertNull(chapter.getCourse()); // потому что @Mapping(target="course", ignore=true)
        assertEquals("Introduction", chapter.getName());
        assertEquals("First chapter", chapter.getDescription());
        assertEquals(1, chapter.getChapterOrder());
    }

    @Test
    void testToDto() {
        Chapter chapter = new Chapter();
        chapter.setId(10L);
        chapter.setName("Java Basics");
        chapter.setDescription("Intro lesson");
        chapter.setChapterOrder(2);
        chapter.setCreatedTime(LocalDateTime.now());
        chapter.setUpdatedTime(LocalDateTime.now());

        Course course = new Course();
        course.setId(1L);
        course.setName("Spring Boot Course");
        chapter.setCourse(course);

        ChapterResponseDto dto = mapper.toDto(chapter);

        assertNotNull(dto);
        assertEquals("Java Basics", dto.getName());
        assertEquals("Intro lesson", dto.getDescription());
        assertEquals(2, dto.getChapterOrder());
        assertEquals(course, dto.getCourse()); // проверяем, что ссылка на курс передаётся
        assertNotNull(dto.getCreatedTime());
        assertNotNull(dto.getUpdatedTime());
    }
}
