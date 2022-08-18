package com.example.onedaypiece.wep.challenge.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChallengeDto {
    private Challenge challenge;

    private Integer challengeMember;

    @Builder
    public ChallengeDto(Challenge challenge, Integer challengeMember) {
        this.challenge = challenge;
        this.challengeMember = challengeMember;
    }

    public static ChallengeDto createChallengeDto(Challenge challenge, Integer challengeMember) {
        return ChallengeDto.builder()
                .challenge(challenge)
                .challengeMember(challengeMember)
                .build();
    }
}
