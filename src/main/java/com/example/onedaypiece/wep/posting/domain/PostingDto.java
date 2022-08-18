package com.example.onedaypiece.wep.posting.domain;

import com.example.onedaypiece.wep.certification.domain.CertificationQueryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class PostingDto {

    private Integer postingId;
    private String postingImg;
    private String postingContent;
    private boolean postingModifyOk;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer postingCount;

    @Builder
    public PostingDto(Integer postingId, String postingImg, String postingContent,
                      boolean postingModifyOk, LocalDateTime createdAt, LocalDateTime modifiedAt,
                      Integer postingCount) {
        this.postingId = postingId;
        this.postingImg = postingImg;
        this.postingContent = postingContent;
        this.postingModifyOk = postingModifyOk;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.postingCount = postingCount;
    }

    public static PostingDto of(PostingListQueryDto posting, List<CertificationQueryDto> certificationList) {

        return PostingDto.builder()
                .postingId(posting.getPostingId())
                .postingImg(posting.getPostingImg())
                .postingContent(posting.getPostingContent())
                .postingCount(posting.getPostingCount())
                .postingModifyOk(posting.isPostingModifyOk())
                .createdAt(posting.getCreatedAt())
                .modifiedAt(posting.getModifiedAt())
                .build();
    }

}
