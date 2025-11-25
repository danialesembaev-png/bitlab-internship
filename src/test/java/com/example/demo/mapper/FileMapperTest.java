package com.example.demo.mapper;

import com.example.demo.dto.FileDto;
import com.example.demo.model.Attachment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileMapperTest {

    private FileMapper fileMapper;

    @BeforeEach
    void setUp() {
        fileMapper = Mappers.getMapper(FileMapper.class);
    }

    @Test
    void testToDto() {
        Attachment file = new Attachment();
        file.setId(1L);
        file.setFileName("file.txt");
        file.setInitialName("original_file.txt");
        file.setMimeType("text/plain");
        file.setFileSize(1024);

        FileDto dto = fileMapper.toDto(file);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("file.txt", dto.getFileName());
        assertEquals("original_file.txt", dto.getInitialName());
        assertEquals("text/plain", dto.getMimeType());
        assertEquals(1024, dto.getFileSize());
    }

    @Test
    void testToEntity() {
        FileDto dto = new FileDto();
        dto.setId(1L);
        dto.setFileName("file.txt");
        dto.setInitialName("original_file.txt");
        dto.setMimeType("text/plain");
        dto.setFileSize(1024);

        Attachment file = fileMapper.toEntity(dto);

        assertNotNull(file);
        assertEquals(1L, file.getId());
        assertEquals("file.txt", file.getFileName());
        assertEquals("original_file.txt", file.getInitialName());
        assertEquals("text/plain", file.getMimeType());
        assertEquals(1024, file.getFileSize());
    }

    @Test
    void testToDtoList() {
        Attachment file1 = new Attachment();
        file1.setId(1L);
        file1.setFileName("file1.txt");
        file1.setInitialName("original1.txt");
        file1.setMimeType("text/plain");
        file1.setFileSize(1024);

        Attachment file2 = new Attachment();
        file2.setId(2L);
        file2.setFileName("file2.txt");
        file2.setInitialName("original2.txt");
        file2.setMimeType("text/plain");
        file2.setFileSize(2048);

        List<FileDto> dtos = fileMapper.toDtoList(List.of(file1, file2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("file1.txt", dtos.get(0).getFileName());
        assertEquals("file2.txt", dtos.get(1).getFileName());
    }
}
