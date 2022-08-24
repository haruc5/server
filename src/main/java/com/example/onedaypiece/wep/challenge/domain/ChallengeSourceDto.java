package com.example.onedaypiece.wep.challenge.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ChallengeSourceDto {
    private Integer challengeId;
    private String challengeTitle;
    private ChallengeCategory challengeCategory;
    private LocalDate challengeStart;
    private LocalDate challengeEnd;
    private String challengeImgUrl;
    private String weekTag;

    @Builder
    public ChallengeSourceDto(Integer challengeId,
                                      String challengeTitle,
                                      ChallengeCategory challengeCategory,
                                      LocalDate challengeStart,
                                      LocalDate challengeEnd,
                                      String challengeImgUrl, String weekTag) {
        this.challengeId = challengeId;
        this.challengeTitle = challengeTitle;
        this.challengeCategory = challengeCategory;
        this.challengeStart = challengeStart;
        this.challengeEnd = challengeEnd;
        this.challengeImgUrl = challengeImgUrl;
        this.weekTag = weekTag;
    }

    public static ChallengeSourceDto createChallengeSourceDto(Challenge challenge) {
        return ChallengeSourceDto.builder()
                .challengeId(challenge.getChallengeId())
                .challengeTitle(challenge.getChallengeTitle())
                .challengeCategory(challenge.getChallengeCategory())
                .challengeStart(challenge.getChallengeStart())
                .challengeEnd(challenge.getChallengeEnd())
                .challengeImgUrl(challenge.getChallengeImgUrl())
                .weekTag(challenge.getWeekTag())
                .build();
    }
}
