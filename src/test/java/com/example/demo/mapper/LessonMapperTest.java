package com.example.demo.mapper;

import com.example.demo.dto.LessonRequestDto;
import com.example.demo.dto.LessonResponseDto;
import com.example.demo.model.Chapter;
import com.example.demo.model.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LessonMapperTest {

    private LessonMapper mapper;

    @BeforeEach
    void setUp() {
        // Берём реальную сгенерированную реализацию MapStruct
        mapper = Mappers.getMapper(LessonMapper.class);
    }

    @Test
    void testToEntity() {
        LessonRequestDto dto = new LessonRequestDto();
        dto.setTitle("Spring Boot");
        dto.setDescription("Intro lesson");
        dto.setContent("Content text");
        dto.setChapterId(5L);

        Lesson lesson = mapper.toEntity(dto);

        assertNotNull(lesson);
        assertNull(lesson.getId()); // потому что @Mapping(target="id", ignore=true)
        assertEquals("Spring Boot", lesson.getTitle());
        assertEquals("Intro lesson", lesson.getDescription());
        // Поле content есть в DTO, но в Lesson его нет → MapStruct его просто игнорирует
    }

    @Test
    void testToDto() {
        Lesson lesson = new Lesson();
        lesson.setId(10L);
        lesson.setTitle("Spring Mapping");
        lesson.setDescription("Mapper test");
        lesson.setLessonOrder(3);
        lesson.setCreatedTime(LocalDateTime.now());
        lesson.setUpdatedTime(LocalDateTime.now());

        Chapter chapter = new Chapter();
        chapter.setId(7L);
        lesson.setChapter(chapter);

        LessonResponseDto dto = mapper.toDto(lesson);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("Spring Mapping", dto.getTitle());
        assertEquals("Mapper test", dto.getDescription());
        assertEquals(3, dto.getLessonOrder());
        // chapterId маппится только если в LessonMapper есть @Mapping для него,
        // но у тебя её нет, поэтому dto.getChapterId() == null
        assertNull(dto.getChapterId());
    }
}
