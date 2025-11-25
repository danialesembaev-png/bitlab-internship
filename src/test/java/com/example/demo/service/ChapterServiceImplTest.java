package com.example.demo.service;

import com.example.demo.dto.ChapterRequestDto;
import com.example.demo.dto.ChapterResponseDto;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Course;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.impl.ChapterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChapterServiceImplTest {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ChapterMapper mapper;

    @InjectMocks
    private ChapterServiceImpl chapterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateChapter_Success() {
        ChapterRequestDto dto = new ChapterRequestDto();
        dto.setName("Chapter 1");
        dto.setDescription("Desc");
        dto.setCourseId(1L);
        dto.setChapterOrder(2);

        Course course = new Course();
        course.setId(1L);

        Chapter chapterEntity = new Chapter();
        Chapter savedChapter = new Chapter();
        savedChapter.setId(10L);

        ChapterResponseDto responseDto = new ChapterResponseDto();
        responseDto.setId(10L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(mapper.toEntity(dto)).thenReturn(chapterEntity);
        when(chapterRepository.save(chapterEntity)).thenReturn(savedChapter);
        when(mapper.toDto(savedChapter)).thenReturn(responseDto);

        ChapterResponseDto result = chapterService.createChapter(dto);

        assertNotNull(result);
        assertEquals(10L, result.getId());

        assertEquals(course, chapterEntity.getCourse());
        assertEquals(2, chapterEntity.getChapterOrder());
        assertNotNull(chapterEntity.getCreatedTime());
        assertNotNull(chapterEntity.getUpdatedTime());

        verify(chapterRepository, times(1)).save(chapterEntity);
    }

    @Test
    void testCreateChapter_NoCourseId_Throws() {
        ChapterRequestDto dto = new ChapterRequestDto();
        dto.setName("Chapter 1");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            chapterService.createChapter(dto);
        });

        assertEquals("courseId is required", exception.getMessage());
    }

    @Test
    void testGetAllChapters() {
        Chapter chapter1 = new Chapter();
        Chapter chapter2 = new Chapter();
        when(chapterRepository.findAll()).thenReturn(List.of(chapter1, chapter2));

        ChapterResponseDto dto1 = new ChapterResponseDto();
        ChapterResponseDto dto2 = new ChapterResponseDto();
        when(mapper.toDto(chapter1)).thenReturn(dto1);
        when(mapper.toDto(chapter2)).thenReturn(dto2);

        List<ChapterResponseDto> result = chapterService.getAllChapters();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void testGetChapterById_Found() {
        Chapter chapter = new Chapter();
        chapter.setId(1L);

        ChapterResponseDto dto = new ChapterResponseDto();
        dto.setId(1L);

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(mapper.toDto(chapter)).thenReturn(dto);

        ChapterResponseDto result = chapterService.getChapterById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetChapterById_NotFound() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            chapterService.getChapterById(1L);
        });

        assertEquals("Chapter not found", exception.getMessage());
    }

    @Test
    void testUpdateChapter() {
        Chapter chapter = new Chapter();
        chapter.setId(1L);
        chapter.setChapterOrder(5);
        chapter.setUpdatedTime(LocalDateTime.now());

        ChapterRequestDto dto = new ChapterRequestDto();
        dto.setName("Updated");
        dto.setDescription("New desc");
        dto.setChapterOrder(10);

        Course course = new Course();
        course.setId(2L);
        dto.setCourseId(2L);

        ChapterResponseDto responseDto = new ChapterResponseDto();
        responseDto.setId(1L);

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(chapterRepository.save(chapter)).thenReturn(chapter);
        when(mapper.toDto(chapter)).thenReturn(responseDto);

        ChapterResponseDto result = chapterService.updateChapter(1L, dto);

        assertNotNull(result);
        assertEquals("Updated", chapter.getName());
        assertEquals("New desc", chapter.getDescription());
        assertEquals(10, chapter.getChapterOrder());
        assertEquals(course, chapter.getCourse());
    }

    @Test
    void testDeleteChapter_Exists() {
        when(chapterRepository.existsById(1L)).thenReturn(true);

        boolean result = chapterService.deleteChapter(1L);

        assertTrue(result);
        verify(chapterRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteChapter_NotExists() {
        when(chapterRepository.existsById(1L)).thenReturn(false);

        boolean result = chapterService.deleteChapter(1L);

        assertFalse(result);
        verify(chapterRepository, never()).deleteById(anyLong());
    }
}
