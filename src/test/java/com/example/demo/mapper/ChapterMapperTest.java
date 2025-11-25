package com.example.demo.mapper;

import com.example.demo.dto.ChapterRequestDto;
import com.example.demo.dto.ChapterResponseDto;
import com.example.demo.model.Chapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class ChapterMapperTest {

    private ChapterMapper chapterMapper;

    @BeforeEach
    void setUp() {
        chapterMapper = Mappers.getMapper(ChapterMapper.class);
    }

    @Test
    void testToEntity() {
        ChapterRequestDto requestDto = new ChapterRequestDto();
        requestDto.setName("Chapter 1");
        requestDto.setDescription("Description of Chapter 1");

        Chapter chapter = chapterMapper.toEntity(requestDto);

        assertNotNull(chapter);
        assertEquals("Chapter 1", chapter.getName());
        assertEquals("Description of Chapter 1", chapter.getDescription());
        assertNull(chapter.getId());
        assertNull(chapter.getCourse());
    }

    @Test
    void testToDto() {
        Chapter chapter = new Chapter();
        chapter.setId(1L);
        chapter.setName("Chapter 1");
        chapter.setDescription("Description of Chapter 1");

        ChapterResponseDto dto = chapterMapper.toDto(chapter);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Chapter 1", dto.getName());
        assertEquals("Description of Chapter 1", dto.getDescription());
    }
}
