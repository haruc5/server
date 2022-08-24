package com.example.onedaypiece.wep.challenge.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChallengeDto {
    private Challenge challenge;


    @Builder
    public ChallengeDto(Challenge challenge) {
        this.challenge = challenge;
    }

    public static ChallengeDto createChallengeDto(Challenge challenge, Integer challengeMember) {
        return ChallengeDto.builder()
                .challenge(challenge)
                .build();
    }
}
