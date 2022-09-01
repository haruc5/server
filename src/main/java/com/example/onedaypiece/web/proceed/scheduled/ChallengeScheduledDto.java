package com.example.onedaypiece.web.proceed.scheduled;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeScheduledDto {
    private List<ScheduledDto> challengeList;

    @Builder
    public ChallengeScheduledDto(List<ScheduledDto> challengeList) {
        this.challengeList = challengeList;
    }

    public static ChallengeScheduledDto createScheduledDto(List<ScheduledDto> challengeList) {
        return ChallengeScheduledDto.builder().challengeList(challengeList).build();
    }
}
