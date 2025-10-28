package com.example.demo.controller;

import com.example.demo.dto.LessonRequestDto;
import com.example.demo.dto.LessonResponseDto;
import com.example.demo.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public LessonResponseDto createLesson(@RequestBody LessonRequestDto dto) {
        return lessonService.createLesson(dto);
    }

    @GetMapping
    public List<LessonResponseDto> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{id}")
    public LessonResponseDto getLessonById(@PathVariable Long id) {
        return lessonService.getLessonById(id);
    }

    @PutMapping("/{id}")
    public LessonResponseDto updateLesson(@PathVariable Long id, @RequestBody LessonRequestDto dto) {
        return lessonService.updateLesson(id, dto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteLesson(@PathVariable Long id) {
        return lessonService.deleteLesson(id);
    }
}
