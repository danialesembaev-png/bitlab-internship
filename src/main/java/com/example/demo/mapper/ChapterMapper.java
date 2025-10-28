package com.example.demo.mapper;

import com.example.demo.dto.ChapterRequestDto;
import com.example.demo.dto.ChapterResponseDto;
import com.example.demo.model.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
        // курс будем ставить вручную в сервисе
    Chapter toEntity(ChapterRequestDto dto);

    ChapterResponseDto toDto(Chapter chapter);
}
