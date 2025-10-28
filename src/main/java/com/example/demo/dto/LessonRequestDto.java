package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LessonRequestDto {
    private String title;
    private String description;
    private String content;
    private Long chapterId;
}
