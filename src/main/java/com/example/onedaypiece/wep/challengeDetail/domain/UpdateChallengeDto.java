package com.example.onedaypiece.wep.challengeDetail.domain;

import com.example.onedaypiece.wep.challenge.domain.ChallengeAuth;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UpdateChallengeDto {
    private Long challengeId;
    private String challengeTitle;
    private String challengeImgUrl;
    private String challengeHoliday;
    private LocalDateTime challengeStart;
    private LocalDateTime challengeEnd;
    private ChallengeAuth challengeAuth;
    private String challengeAuthMethod;
    private String challengePassword;
    private String challengeContent;
}
