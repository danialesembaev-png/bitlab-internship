package com.example.demo.mapper;

import com.example.demo.dto.LessonRequestDto;
import com.example.demo.dto.LessonResponseDto;
import com.example.demo.model.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "id", ignore = true)
    Lesson toEntity(LessonRequestDto dto);

    LessonResponseDto toDto(Lesson lesson);
}
