package com.dubbi.imageserver.application.service;

import com.dubbi.imageserver.domain.entity.Image;
import com.dubbi.imageserver.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final FileStorageService fileStorageService;

    public Image storeImage(MultipartFile file){
        String fileName = fileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();
    Image image = Image.builder()
            .name(fileName)
            .url(fileDownloadUri)
            .contentType(file.getContentType()).build();
    return imageRepository.save(image);
    }

    public Image getImageByUrl(String url){
        return imageRepository.findByUrl(url).orElseThrow(() -> new RuntimeException("Image not found with url" + url ));
    }

    public Resource loadFileAsResource(String fileName){
        return fileStorageService.loadFileAsResource(fileName);
    }

}
