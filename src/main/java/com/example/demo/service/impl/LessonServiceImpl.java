package com.example.demo.service.impl;

import com.example.demo.dto.LessonRequestDto;
import com.example.demo.dto.LessonResponseDto;
import com.example.demo.mapper.LessonMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Lesson;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.service.LessonService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final LessonMapper mapper;

    public LessonServiceImpl(LessonRepository lessonRepository,
                             ChapterRepository chapterRepository,
                             LessonMapper mapper) {
        this.lessonRepository = lessonRepository;
        this.chapterRepository = chapterRepository;
        this.mapper = mapper;
    }

    @Override
    public LessonResponseDto createLesson(LessonRequestDto dto) {
        Lesson lesson = mapper.toEntity(dto);

        Chapter chapter = chapterRepository.findById(dto.getChapterId())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
        lesson.setChapter(chapter);

        Integer maxOrder = lessonRepository.findMaxLessonOrderByChapterId(chapter.getId());
        int nextOrder = (maxOrder == null) ? 1 : maxOrder + 1;
        lesson.setLessonOrder(nextOrder);

        lesson.setCreatedTime(LocalDateTime.now());
        lesson.setUpdatedTime(LocalDateTime.now());

        Lesson saved = lessonRepository.save(lesson);
        return mapper.toDto(saved);
    }

    @Override
    public List<LessonResponseDto> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LessonResponseDto getLessonById(Long id) {
        return lessonRepository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public LessonResponseDto updateLesson(Long id, LessonRequestDto dto) {
        return lessonRepository.findById(id)
                .map(existing -> {
                    if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
                    if (dto.getDescription() != null) existing.setDescription(dto.getDescription());

                    existing.setUpdatedTime(LocalDateTime.now());
                    Lesson updated = lessonRepository.save(existing);
                    return mapper.toDto(updated);
                })
                .orElse(null);
    }

    @Override
    public boolean deleteLesson(Long id) {
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
