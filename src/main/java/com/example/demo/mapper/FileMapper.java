package com.example.demo.mapper;

import com.example.demo.dto.FileDto;
import com.example.demo.model.Attachment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {


    FileDto toDto(Attachment attachment);

    Attachment toEntity(FileDto fileDto);

    List<FileDto> toDtoList(List<Attachment> files);


}
