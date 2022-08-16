package com.example.onedaypiece.wep.posting.domain;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Getter
public class UpdatePostingDto {
    private String postingImg;
    private String postingContent;

    @Builder
    public UpdatePostingDto(String postingImg, String postingContent){
        this.postingImg = postingImg;
        this.postingContent = postingContent;
    }
}
