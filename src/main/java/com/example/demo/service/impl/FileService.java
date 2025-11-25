package com.example.demo.service.impl;

import com.example.demo.dto.FileDto;
import com.example.demo.mapper.FileMapper;
import com.example.demo.model.Attachment;
import com.example.demo.model.Lesson;
import com.example.demo.repository.FileRepository;
import com.example.demo.repository.LessonRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class FileService {

    private final MinioClient minioClient;
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final LessonRepository lessonRepository;

    @Value("${minio.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file, Long
            lessonId) {

        try {
            Optional<Lesson> lesson = lessonRepository.findById(lessonId);
            if (lesson.isEmpty()) {
                throw new RuntimeException("Lesson Not Found");
            }

            Attachment attachment = new Attachment();
            attachment.setLesson(lesson.get());
            attachment.setFileSize(file.getSize());
            attachment.setMimeType(file.getContentType());
            attachment.setInitialName(file.getOriginalFilename());

            attachment = fileRepository.save(attachment);
            if (attachment.getId() != null) {

                String fileName = DigestUtils.sha1Hex(attachment.getId() + "_My_file") + "." + FileNameUtils.getExtension(file.getOriginalFilename());
                attachment.setFileName(fileName);


                minioClient.putObject(
                        PutObjectArgs
                                .builder()
                                .bucket(bucket)
                                .object(fileName)
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );

                fileRepository.save(attachment);


                return "File uploaded successfully!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Some errors on file upload ";
    }

    public ByteArrayResource downloadFile(String fileName) {
        try {

            GetObjectArgs getObjectArgs = GetObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(fileName)
                    .build();

            InputStream stream = minioClient.getObject(getObjectArgs);
            byte[] byteArray = IOUtils.toByteArray(stream);
            stream.close();

            return new ByteArrayResource(byteArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;


    }

    public List<FileDto> getFileList() {
        return fileMapper.toDtoList(fileRepository.findAll());
    }


}
