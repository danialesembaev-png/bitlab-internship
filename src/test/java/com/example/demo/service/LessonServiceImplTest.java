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
import static org.mockito.ArgumentMatchers.any;
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

    // ------------------ CREATE LESSON ------------------
    @Test
    void testCreateLessonSuccess() {
        LessonRequestDto dto = new LessonRequestDto();
        dto.setTitle("Lesson 1");
        dto.setDescription("Description");
        dto.setChapterId(10L);

        Lesson lesson = new Lesson();
        lesson.setTitle(dto.getTitle());

        Chapter chapter = new Chapter();
        chapter.setId(10L);

        Lesson savedLesson = new Lesson();
        savedLesson.setId(1L);
        savedLesson.setTitle("Lesson 1");

        LessonResponseDto responseDto = new LessonResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Lesson 1");

        when(mapper.toEntity(any())).thenReturn(lesson);
        when(chapterRepository.findById(10L)).thenReturn(Optional.of(chapter));
        when(lessonRepository.findMaxLessonOrderByChapterId(10L)).thenReturn(3);
        when(lessonRepository.save(any())).thenReturn(savedLesson);
        when(mapper.toDto(any())).thenReturn(responseDto);

        LessonResponseDto result = lessonService.createLesson(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Lesson 1", result.getTitle());

        verify(mapper).toEntity(dto);
        verify(lessonRepository).save(any());
    }

    // ------------------ GET ALL LESSONS ------------------
    @Test
    void testGetAllLessons() {
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);

        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);

        LessonResponseDto dto1 = new LessonResponseDto();
        dto1.setId(1L);

        LessonResponseDto dto2 = new LessonResponseDto();
        dto2.setId(2L);

        when(lessonRepository.findAll()).thenReturn(List.of(lesson1, lesson2));
        when(mapper.toDto(lesson1)).thenReturn(dto1);
        when(mapper.toDto(lesson2)).thenReturn(dto2);

        List<LessonResponseDto> list = lessonService.getAllLessons();

        assertEquals(2, list.size());
        verify(lessonRepository).findAll();
    }

    // ------------------ GET LESSON BY ID ------------------
    @Test
    void testGetLessonById_found() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);

        LessonResponseDto dto = new LessonResponseDto();
        dto.setId(1L);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(mapper.toDto(lesson)).thenReturn(dto);

        LessonResponseDto result = lessonService.getLessonById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetLessonById_notFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());
        LessonResponseDto result = lessonService.getLessonById(1L);
        assertNull(result);
    }

    // ------------------ UPDATE LESSON ------------------
    @Test
    void testUpdateLessonSuccess() {
        LessonRequestDto dto = new LessonRequestDto();
        dto.setTitle("Updated");

        Lesson existing = new Lesson();
        existing.setId(1L);
        existing.setTitle("Old Title");

        Lesson updated = new Lesson();
        updated.setId(1L);
        updated.setTitle("Updated");

        LessonResponseDto responseDto = new LessonResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Updated");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(lessonRepository.save(any())).thenReturn(updated);
        when(mapper.toDto(updated)).thenReturn(responseDto);

        LessonResponseDto result = lessonService.updateLesson(1L, dto);

        assertNotNull(result);
        assertEquals("Updated", result.getTitle());
    }

    @Test
    void testUpdateLesson_notFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());
        LessonResponseDto result = lessonService.updateLesson(1L, new LessonRequestDto());
        assertNull(result);
    }

    // ------------------ DELETE LESSON ------------------
    @Test
    void testDeleteLessonSuccess() {
        when(lessonRepository.existsById(5L)).thenReturn(true);

        boolean deleted = lessonService.deleteLesson(5L);

        assertTrue(deleted);
        verify(lessonRepository).deleteById(5L);
    }

    @Test
    void testDeleteLesson_notFound() {
        when(lessonRepository.existsById(5L)).thenReturn(false);

        boolean deleted = lessonService.deleteLesson(5L);

        assertFalse(deleted);
        verify(lessonRepository, never()).deleteById(any());
    }
}
