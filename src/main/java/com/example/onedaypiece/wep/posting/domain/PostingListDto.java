package com.example.onedaypiece.wep.posting.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class PostingListDto {
    private List<PostingDto> postList = new ArrayList<>();
    private boolean hasNext;

    @Builder
    public PostingListDto(List<PostingDto> postList, boolean hasNext){
        this.postList = postList;
        this.hasNext = hasNext;
    }

    public static PostingListDto createPostingListDto(List<PostingDto> postingDto, boolean hasNext){
        return PostingListDto.builder()
                .postList(postingDto)
                .hasNext(hasNext)
                .build();
    }
}
