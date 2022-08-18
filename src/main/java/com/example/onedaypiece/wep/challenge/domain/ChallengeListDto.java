package com.example.onedaypiece.wep.challenge.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeListDto {
    private List<ChallengeDto> challengeList = new ArrayList<>();
    private boolean hasNext;

    @Builder
    public ChallengeListDto(List<ChallengeDto> challengeList, boolean hasNext){
        this.challengeList = challengeList;
        this.hasNext = hasNext;
    }

    public static ChallengeListDto createChallengeListDto(List<ChallengeDto> dto, boolean hasNext){
        return ChallengeListDto.builder()
                .challengeList(dto)
                .hasNext(hasNext)
                .build();
    }
}
