package com.example.onedaypiece.web.certification.domain;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CertificationQueryDto {
    private Integer postingId;

    @QueryProjection
    public CertificationQueryDto(Integer postingId){
        this.postingId = postingId;
    }
}
