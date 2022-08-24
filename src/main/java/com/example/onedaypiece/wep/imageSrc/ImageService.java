package com.example.onedaypiece.wep.imageSrc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public Integer saveImage(Image image) {
        return imageRepository.save(image).getImageId();
    }
}
