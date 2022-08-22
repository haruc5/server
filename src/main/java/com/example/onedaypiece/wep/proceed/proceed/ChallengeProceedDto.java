package com.example.onedaypiece.wep.proceed.proceed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeProceedDto {
    private List<ProceedDto> challengeList;

    @Builder
    public ChallengeProceedDto(List<ProceedDto> challengeList) {
        this.challengeList = challengeList;
    }

    public static ChallengeProceedDto createProceedDto(List<ProceedDto> challengeList) {
        return ChallengeProceedDto.builder().challengeList(challengeList).build();
    }
}
