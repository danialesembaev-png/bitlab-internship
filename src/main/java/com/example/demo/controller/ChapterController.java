package com.example.demo.controller;

import com.example.demo.dto.ChapterRequestDto;
import com.example.demo.dto.ChapterResponseDto;
import com.example.demo.service.ChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @PostMapping
    public ResponseEntity<ChapterResponseDto> createChapter(@RequestBody ChapterRequestDto dto) {
        try {
            ChapterResponseDto response = chapterService.createChapter(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ChapterResponseDto>> getAllChapters() {
        List<ChapterResponseDto> chapters = chapterService.getAllChapters();
        return ResponseEntity.ok(chapters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterResponseDto> getChapterById(@PathVariable Long id) {
        ChapterResponseDto chapter = chapterService.getChapterById(id);
        if (chapter != null) {
            return ResponseEntity.ok(chapter);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChapterResponseDto> updateChapter(@PathVariable Long id,
                                                            @RequestBody ChapterRequestDto dto) {
        ChapterResponseDto updated = chapterService.updateChapter(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        boolean deleted = chapterService.deleteChapter(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
