package com.example.demo.service;

import com.example.demo.dto.ChapterRequestDto;
import com.example.demo.dto.ChapterResponseDto;

import java.util.List;

public interface ChapterService {


    ChapterResponseDto createChapter(ChapterRequestDto dto);

    List<ChapterResponseDto> getAllChapters();

    ChapterResponseDto getChapterById(Long id);

    ChapterResponseDto updateChapter(Long id, ChapterRequestDto dto);

    boolean deleteChapter(Long id);
}
