package com.example.onedaypiece.wep.challenge.domain;

import com.example.onedaypiece.wep.challengeDetail.domain.ChallengeDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeSourceDto {
    private Integer challengeId;
    private String challengeTitle;
    private ChallengeCategory challengeCategory;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate challengeStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate challengeEnd;
    private String challengeImgUrl;
    private Integer viewCount;
    private String weekTag;

    @Builder
    public ChallengeSourceDto(Integer challengeId,
                              String challengeTitle,
                              ChallengeCategory challengeCategory,
                              LocalDate challengeStart,
                              LocalDate challengeEnd,
                              String challengeImgUrl,
                              Integer viewCount,
                              String weekTag) {
        this.challengeId = challengeId;
        this.challengeTitle = challengeTitle;
        this.challengeCategory = challengeCategory;
        this.challengeStart = challengeStart;
        this.challengeEnd = challengeEnd;
        this.challengeImgUrl = challengeImgUrl;
        this.viewCount = viewCount;
        this.weekTag = weekTag;
    }

    public static ChallengeSourceDto createChallengeSourceDto(Challenge challenge,
                                                              List<ChallengeDetail> details) {
        return ChallengeSourceDto.builder()
                .challengeId(challenge.getChallengeId())
                .challengeTitle(challenge.getChallengeTitle())
                .challengeCategory(challenge.getChallengeCategory())
                .challengeStart(challenge.getChallengeStart())
                .challengeEnd(challenge.getChallengeEnd())
                .challengeImgUrl(challenge.getChallengeImgUrl())
                .viewCount(challenge.getViewCount())
                .weekTag(challenge.getWeekTag())
                .build();
    }
}
