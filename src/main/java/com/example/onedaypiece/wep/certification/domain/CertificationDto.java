package com.example.onedaypiece.wep.certification.domain;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class CertificationDto {

    private Integer postingId;
    private Integer totalNumber;

    @Builder
    public CertificationDto(Integer postingId, Integer totalNumber){
        this.postingId = postingId;
        this.totalNumber = totalNumber;
    }
}
