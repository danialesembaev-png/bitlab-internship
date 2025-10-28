package com.example.demo.service;

import com.example.demo.dto.LessonRequestDto;
import com.example.demo.dto.LessonResponseDto;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Lesson;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LessonServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private LessonMapper mapper;

    @InjectMocks
    private LessonServiceImpl lessonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLesson() {
        LessonRequestDto dto = new LessonRequestDto();
        dto.setChapterId(1L);
        dto.setTitle("Intro");
        dto.setDescription("Basics");

        Chapter chapter = new Chapter();
        chapter.setId(1L);

        Lesson lesson = new Lesson();
        lesson.setTitle("Intro");
        lesson.setChapter(chapter);

        LessonResponseDto response = new LessonResponseDto();
        response.setTitle("Intro");

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(mapper.toEntity(dto)).thenReturn(lesson);
        when(lessonRepository.findMaxLessonOrderByChapterId(1L)).thenReturn(0);
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);
        when(mapper.toDto(lesson)).thenReturn(response);

        LessonResponseDto result = lessonService.createLesson(dto);

        assertNotNull(result);
        assertEquals("Intro", result.getTitle());
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void testCreateLesson_ChapterNotFound() {
        LessonRequestDto dto = new LessonRequestDto();
        dto.setChapterId(1L);

        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> lessonService.createLesson(dto));

        assertEquals("Chapter not found", exception.getMessage());
    }

    @Test
    void testGetAllLessons() {
        Lesson lesson = new Lesson();
        lesson.setTitle("Lesson 1");

        LessonResponseDto dto = new LessonResponseDto();
        dto.setTitle("Lesson 1");

        when(lessonRepository.findAll()).thenReturn(List.of(lesson));
        when(mapper.toDto(lesson)).thenReturn(dto);

        List<LessonResponseDto> result = lessonService.getAllLessons();

        assertEquals(1, result.size());
        assertEquals("Lesson 1", result.get(0).getTitle());
    }

    @Test
    void testGetLessonById() {
        Lesson lesson = new Lesson();
        lesson.setTitle("Lesson 1");

        LessonResponseDto dto = new LessonResponseDto();
        dto.setTitle("Lesson 1");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(mapper.toDto(lesson)).thenReturn(dto);

        LessonResponseDto result = lessonService.getLessonById(1L);

        assertNotNull(result);
        assertEquals("Lesson 1", result.getTitle());
    }

    @Test
    void testGetLessonById_NotFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        LessonResponseDto result = lessonService.getLessonById(1L);

        assertNull(result);
    }

    @Test
    void testUpdateLesson() {
        Lesson existing = new Lesson();
        existing.setId(1L);
        existing.setTitle("Old title");

        LessonRequestDto dto = new LessonRequestDto();
        dto.setTitle("New title");

        Lesson updated = new Lesson();
        updated.setTitle("New title");

        LessonResponseDto response = new LessonResponseDto();
        response.setTitle("New title");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(updated);
        when(mapper.toDto(updated)).thenReturn(response);

        LessonResponseDto result = lessonService.updateLesson(1L, dto);

        assertNotNull(result);
        assertEquals("New title", result.getTitle());
    }

    @Test
    void testDeleteLesson() {
        when(lessonRepository.existsById(1L)).thenReturn(true);

        boolean result = lessonService.deleteLesson(1L);

        assertTrue(result);
        verify(lessonRepository).deleteById(1L);
    }

    @Test
    void testDeleteLesson_NotExists() {
        when(lessonRepository.existsById(1L)).thenReturn(false);

        boolean result = lessonService.deleteLesson(1L);

        assertFalse(result);
        verify(lessonRepository, never()).deleteById(any());
    }
}
