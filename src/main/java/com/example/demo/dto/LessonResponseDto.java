package com.example.demo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
