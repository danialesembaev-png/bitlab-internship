package com.example.demo.dto;

import lombok.Data;

@Data
public class ChapterRequestDto {
    private String name;
    private String description;
    private Integer chapterOrder;
    private Long courseId;
}
