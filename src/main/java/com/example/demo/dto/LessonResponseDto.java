package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LessonResponseDto {
    private Long id;
    private String title;
    private String description;
    private String content;
    private Integer lessonOrder;
    private Long chapterId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
