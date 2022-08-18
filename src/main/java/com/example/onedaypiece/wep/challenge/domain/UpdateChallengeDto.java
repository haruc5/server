package com.example.onedaypiece.wep.challenge.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class UpdateChallengeDto {
    private Integer challengeId;
    private String challengeTitle;
    private ChallengeCategory challengeCategory;
    private String challengeImgUrl;
    private String challengeHoliday;
    private LocalDate challengeStart;
    private LocalDate challengeEnd;
    private ChallengeAuth challengeAuth;
    private String challengeAuthMethod;
    private String challengePassword;
    private String challengeContent;
}
