package com.example.demo.service;

import com.example.demo.dto.LessonRequestDto;
import com.example.demo.dto.LessonResponseDto;

import java.util.List;

public interface LessonService {

    // Создать новый урок
    LessonResponseDto createLesson(LessonRequestDto dto);

    // Получить все уроки
    List<LessonResponseDto> getAllLessons();

    // Получить урок по ID
    LessonResponseDto getLessonById(Long id);

    // Обновить урок по ID
    LessonResponseDto updateLesson(Long id, LessonRequestDto dto);

    // Удалить урок по ID
    boolean deleteLesson(Long id);
}
