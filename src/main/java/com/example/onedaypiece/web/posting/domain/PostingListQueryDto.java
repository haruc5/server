package com.example.onedaypiece.web.posting.domain;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
public class PostingListQueryDto {
    private Integer postingId;
    private String postingImg;
    private String postingContent;
    private boolean postingModifyOk;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer postingCount;

    @QueryProjection
    public PostingListQueryDto(Integer postingId, String postingImg, String postingContent,
                               boolean postingModifyOk, LocalDateTime createdAt, LocalDateTime modifiedAt,
                               Integer postingCount){
        this.postingId = postingId;
        this.postingImg = postingImg;
        this.postingContent = postingContent;
        this.postingModifyOk = postingModifyOk;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.postingCount = postingCount;
    }
}
