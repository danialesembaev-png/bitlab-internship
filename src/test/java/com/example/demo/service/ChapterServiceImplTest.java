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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    // ✅ 1. Создание главы
    @Test
    void createChapter_ShouldReturnSavedChapter() {
        ChapterRequestDto dto = new ChapterRequestDto();
        dto.setCourseId(1L);
        dto.setName("Intro");
        dto.setChapterOrder(1);

        Course course = new Course();
        course.setId(1L);

        Chapter chapter = new Chapter();
        chapter.setName("Intro");
        chapter.setCourse(course);
        chapter.setCreatedTime(LocalDateTime.now());
        chapter.setUpdatedTime(LocalDateTime.now());

        ChapterResponseDto response = new ChapterResponseDto();
        response.setName("Intro");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(mapper.toEntity(dto)).thenReturn(chapter);
        when(chapterRepository.save(any(Chapter.class))).thenReturn(chapter);
        when(mapper.toDto(chapter)).thenReturn(response);

        ChapterResponseDto result = chapterService.createChapter(dto);

        assertNotNull(result);
        assertEquals("Intro", result.getName());
        verify(chapterRepository, times(1)).save(any(Chapter.class));
    }

    // ✅ 2. Ошибка при отсутствии courseId
    @Test
    void createChapter_ShouldThrowException_WhenCourseIdMissing() {
        ChapterRequestDto dto = new ChapterRequestDto();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chapterService.createChapter(dto));

        assertEquals("courseId is required", exception.getMessage());
    }

    // ✅ 3. Ошибка при отсутствии курса
    @Test
    void createChapter_ShouldThrowException_WhenCourseNotFound() {
        ChapterRequestDto dto = new ChapterRequestDto();
        dto.setCourseId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chapterService.createChapter(dto));

        assertEquals("Course not found", exception.getMessage());
    }

    // ✅ 4. Получение по ID
    @Test
    void getChapterById_ShouldReturnChapter() {
        Chapter chapter = new Chapter();
        chapter.setName("Chapter 1");

        ChapterResponseDto dto = new ChapterResponseDto();
        dto.setName("Chapter 1");

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(mapper.toDto(chapter)).thenReturn(dto);

        ChapterResponseDto result = chapterService.getChapterById(1L);

        assertThat(result.getName()).isEqualTo("Chapter 1");
    }

    // ✅ 5. Ошибка, если главы нет
    @Test
    void getChapterById_ShouldThrowException_WhenNotFound() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chapterService.getChapterById(1L));

        assertEquals("Chapter not found", exception.getMessage());
    }

    // ✅ 6. Удаление, если глава существует
    @Test
    void deleteChapter_ShouldInvokeRepositoryDelete_WhenExists() {
        when(chapterRepository.existsById(1L)).thenReturn(true);

        boolean result = chapterService.deleteChapter(1L);

        assertTrue(result);
        verify(chapterRepository, times(1)).deleteById(1L);
    }

    // ✅ 7. Не удаляет, если главы нет
    @Test
    void deleteChapter_ShouldReturnFalse_WhenNotExists() {
        when(chapterRepository.existsById(1L)).thenReturn(false);

        boolean result = chapterService.deleteChapter(1L);

        assertFalse(result);
        verify(chapterRepository, never()).deleteById(any());
    }
}
