package com.example.onedaypiece.wep.imageSrc;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    @Column(length = 500, nullable = false)
    private String originImageName;

    @Column(length = 500, nullable = false)
    private String imageName;

    @Column(length = 1000, nullable = false)
    private String imagePath;

    @Builder
    public Image(String originImageName, String imageName, String imagePath) {
        this.originImageName = originImageName;
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    public static Image createImage(Image image) {
        return Image.builder()
                .originImageName(image.originImageName)
                .imageName(image.imageName)
                .imagePath(image.imagePath)
                .build();
    }
}
