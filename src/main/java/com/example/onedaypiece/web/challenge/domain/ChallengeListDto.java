package com.example.onedaypiece.web.challenge.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeListDto {
    private List<ChallengeDto> challengeList = new ArrayList<>();

    @Builder
    public ChallengeListDto(List<ChallengeDto> challengeList){
        this.challengeList = challengeList;
    }

    public static ChallengeListDto createChallengeListDto(List<ChallengeDto> dto){
        return ChallengeListDto.builder()
                .challengeList(dto)
                .build();
    }
}
