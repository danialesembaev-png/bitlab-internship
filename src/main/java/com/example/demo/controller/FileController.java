package com.example.demo.controller;

import com.example.demo.dto.FileDto;
import com.example.demo.service.impl.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/file")
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload")
    public String upload(@RequestParam(name = "file") MultipartFile file, @RequestParam("lesson_id") Long lessonId) {
        return fileService.uploadFile(file, lessonId);
    }

    @GetMapping(value = "/download/{file}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "file") String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(fileService.downloadFile(fileName));

    }

    @GetMapping(value = "/list")
    public List<FileDto> fileList() {
        return fileService.getFileList();

    }
}
