package com.example.demo.dto;

import com.example.demo.model.Course;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChapterResponseDto {
    private String name;
    private String description;
    private Integer chapterOrder;
    private Course course;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

}
