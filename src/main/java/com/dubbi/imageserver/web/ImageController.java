package com.dubbi.imageserver.web;

import com.dubbi.imageserver.application.dto.ImageResponseDto;
import com.dubbi.imageserver.application.service.ImageService;
import com.dubbi.imageserver.domain.entity.Image;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageResponseDto> uploadImage(@RequestParam("image")MultipartFile file){
        Image storedImage = imageService.storeImage(file);

        ImageResponseDto responseDto = ImageResponseDto.builder()
                .id(storedImage.getId())
                .name(storedImage.getName())
                .url(storedImage.getUrl())
                .contentType(storedImage.getContentType()).build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/url")
    public ResponseEntity<Resource> getImageByUrl(@RequestParam String url, HttpServletRequest request){
        Image image = imageService.getImageByUrl(url);

        Resource resource = imageService.loadFileAsResource(image.getName());

        String contentType = null;
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(contentType == null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + resource)
                .body(resource);
    }
}
