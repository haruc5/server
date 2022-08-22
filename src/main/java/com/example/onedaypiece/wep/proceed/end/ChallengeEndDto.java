package com.example.onedaypiece.wep.proceed.end;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeEndDto {
    private List<EndDto> challengeList;

    @Builder
    public ChallengeEndDto(List<EndDto> challengeList) {
        this.challengeList = challengeList;
    }

    public static ChallengeEndDto createEndDto(List<EndDto> challengeList) {
        return ChallengeEndDto.builder().challengeList(challengeList).build();
    }
}
