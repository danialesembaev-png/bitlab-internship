package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class FileDto {

    private Long id;

    private String fileName;

    private String initialName;

    private String mimeType;

    private long fileSize;


}
