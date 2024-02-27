package com.dubbi.imageserver.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ImageResponseDto {
    private Long id;
    private String name;
    private String url;
    private String contentType;
}
