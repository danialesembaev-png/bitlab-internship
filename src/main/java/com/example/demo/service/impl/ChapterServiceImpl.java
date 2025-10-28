package com.example.demo.service.impl;

import com.example.demo.dto.ChapterRequestDto;
import com.example.demo.dto.ChapterResponseDto;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Course;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.ChapterService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final ChapterMapper mapper;

    public ChapterServiceImpl(ChapterRepository chapterRepository,
                              CourseRepository courseRepository,
                              ChapterMapper mapper) {
        this.chapterRepository = chapterRepository;
        this.courseRepository = courseRepository;
        this.mapper = mapper;
    }

    @Override
    public ChapterResponseDto createChapter(ChapterRequestDto dto) {
        if (dto.getCourseId() == null) {
            throw new RuntimeException("courseId is required");
        }

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Chapter chapter = mapper.toEntity(dto);
        chapter.setChapterOrder(dto.getChapterOrder() != null ? dto.getChapterOrder() : 0);
        chapter.setCourse(course);
        chapter.setCreatedTime(LocalDateTime.now());
        chapter.setUpdatedTime(LocalDateTime.now());

        return mapper.toDto(chapterRepository.save(chapter));
    }

    @Override
    public List<ChapterResponseDto> getAllChapters() {
        return chapterRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChapterResponseDto getChapterById(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
        return mapper.toDto(chapter);
    }

    @Override
    public ChapterResponseDto updateChapter(Long id, ChapterRequestDto dto) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        chapter.setName(dto.getName());
        chapter.setDescription(dto.getDescription());
        chapter.setChapterOrder(dto.getChapterOrder() != null ? dto.getChapterOrder() : chapter.getChapterOrder());
        chapter.setUpdatedTime(LocalDateTime.now());

        if (dto.getCourseId() != null) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            chapter.setCourse(course);
        }

        return mapper.toDto(chapterRepository.save(chapter));
    }

    @Override
    public boolean deleteChapter(Long id) {
        if (chapterRepository.existsById(id)) {
            chapterRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
