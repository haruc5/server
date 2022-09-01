package com.example.onedaypiece.web.proceed.scheduled;

import com.example.onedaypiece.web.challenge.domain.Challenge;
import com.example.onedaypiece.web.challenge.domain.ChallengeCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ScheduledDto {
    private Integer challengeId;
    private String challengeTitle;
    private String challengeContent;
    private ChallengeCategory challengeCategory;
    private Long challengeProgress;
    private String challengeImgUrl;
    private LocalDate challengeStart;
    private LocalDate challengeEnd;
    private int successPercent;

    public ScheduledDto(Challenge challenge) {
        this.challengeId = challenge.getChallengeId();
        this.challengeTitle = challenge.getChallengeTitle();
        this.challengeContent = challenge.getChallengeContent();
        this.challengeCategory = challenge.getChallengeCategory();
        this.challengeProgress = challenge.getChallengeProgress();
        this.challengeImgUrl = challenge.getChallengeImgUrl();
        this.challengeStart = challenge.getChallengeStart();
        this.challengeEnd = challenge.getChallengeEnd();
        this.successPercent = 0;
    }

}
