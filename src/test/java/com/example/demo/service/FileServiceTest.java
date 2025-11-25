package com.example.demo.service;

import com.example.demo.dto.FileDto;
import com.example.demo.mapper.FileMapper;
import com.example.demo.model.Attachment;
import com.example.demo.model.Lesson;
import com.example.demo.repository.FileRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.service.impl.FileService;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.FileNameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileServiceTest {

    private MinioClient minioClient;
    private FileRepository fileRepository;
    private LessonRepository lessonRepository;
    private FileMapper fileMapper;
    private FileService fileService;

    @BeforeEach
    void setUp() {
        minioClient = mock(MinioClient.class);
        fileRepository = mock(FileRepository.class);
        lessonRepository = mock(LessonRepository.class);
        fileMapper = mock(FileMapper.class);

        fileService = new FileService(minioClient, fileRepository, fileMapper, lessonRepository);

        try {
            java.lang.reflect.Field field = FileService.class.getDeclaredField("bucket");
            field.setAccessible(true);
            field.set(fileService, "test-bucket");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUploadFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Hello World".getBytes()
        );

        Lesson lesson = new Lesson();
        lesson.setId(1L);

        Attachment savedFile = new Attachment();
        savedFile.setId(1L);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(fileRepository.save(any(Attachment.class))).thenReturn(savedFile);

        String result = fileService.uploadFile(multipartFile, 1L);

        assertEquals("File uploaded successfully!", result);

        ArgumentCaptor<PutObjectArgs> captor = ArgumentCaptor.forClass(PutObjectArgs.class);
        verify(minioClient).putObject(captor.capture());

        PutObjectArgs args = captor.getValue();
        assertEquals("test-bucket", args.bucket());
        assertEquals("text/plain", args.contentType());

        String expectedFileName = DigestUtils.sha1Hex(savedFile.getId() + "_My_file") + "." +
                FileNameUtils.getExtension(multipartFile.getOriginalFilename());
        assertEquals(expectedFileName, savedFile.getFileName());
    }

    @Test
    void testDownloadFile() throws Exception {
        String fileName = "test-file.txt";
        byte[] fileContent = "Hello".getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        GetObjectResponse response = mock(GetObjectResponse.class);
        when(response.readAllBytes()).thenReturn(fileContent);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(response);

        ByteArrayResource resource = fileService.downloadFile(fileName);

        assertNotNull(resource);
        assertArrayEquals(fileContent, resource.getByteArray());
    }

    @Test
    void testGetFileList() {
        Attachment file1 = new Attachment();
        Attachment file2 = new Attachment();

        when(fileRepository.findAll()).thenReturn(List.of(file1, file2));
        when(fileMapper.toDtoList(anyList())).thenReturn(List.of(new FileDto(), new FileDto()));

        List<FileDto> result = fileService.getFileList();

        assertEquals(2, result.size());
        verify(fileRepository).findAll();
        verify(fileMapper).toDtoList(anyList());
    }
}