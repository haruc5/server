package com.example.onedaypiece.wep.imageSrc;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String displayName;
    private Integer size;
    private Integer count;
    private LocalDateTime uploadDateTime;

    @Builder
    public UploadFile(String displayName) {
        this.displayName = displayName;
        this.uploadDateTime = LocalDateTime.now();
    }

//    public static UploadFile createImage(UploadFile image) {
//        return UploadFile.builder()
//                .displayName()
//                .size(file.getSize())
//                .count(fileTargetCount)
//                .build();
//    }
}
