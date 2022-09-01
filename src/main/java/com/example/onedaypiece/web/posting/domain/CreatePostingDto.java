package com.example.onedaypiece.web.posting.domain;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Data
public class CreatePostingDto {
    private String postingImg;
    private String postingContent;
    private Integer challengeId;
    private Integer totalNumber;

    @Builder
    public CreatePostingDto(String postingImg, String postingContent,
                            Integer challengeId, Integer totalNumber){
        this.postingImg = postingImg;
        this.postingContent = postingContent;
        this.challengeId = challengeId;
        this.totalNumber = totalNumber;
    }
}
