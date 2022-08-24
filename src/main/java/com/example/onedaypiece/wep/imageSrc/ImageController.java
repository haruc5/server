package com.example.onedaypiece.wep.imageSrc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("api/upload/image")
    public String uploadImage(@RequestParam(name = "image") MultipartFile image) throws IOException {
        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "image"; // 새로 부여한 이미지명
        String fileExtension = '.' + image.getOriginalFilename().replaceAll("^.*\\.(.*)$", "$1");
        String path = "C:\\work\\oneday-piece\\src\\main\\resources\\static\\imgs"; // 저장될 폴더 경로


        try {
            if(!image.isEmpty()) {
                File file = new File(path);
                if(!file.exists()) {
                    file.mkdirs();
                }
                file = new File(path+"\\"+fileName);
                image.transferTo(file);
                Image upLoadImage = Image.builder()
                        .originImageName(image.getOriginalFilename())
                        .imagePath(path)
                        .imageName(fileName + fileExtension)
                        .build();
                imageService.saveImage(upLoadImage);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }
}
