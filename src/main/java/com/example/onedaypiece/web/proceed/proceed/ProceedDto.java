package com.example.onedaypiece.web.proceed.proceed;

import com.example.onedaypiece.web.challenge.domain.Challenge;
import com.example.onedaypiece.web.challenge.domain.ChallengeCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@Getter
public class ProceedDto {
    private Integer challengeId;
    private String challengeTitle;
    private String challengeContent;
    private ChallengeCategory challengeCategory;
    private Long challengeProgress;
    private String challengeImgUrl;
    private LocalDate challengeStart;
    private LocalDate challengeEnd;
    private int successPercent;

    public ProceedDto(Challenge challenge) {
        this.challengeId = challenge.getChallengeId();
        this.challengeTitle = challenge.getChallengeTitle();
        this.challengeContent = challenge.getChallengeContent();
        this.challengeCategory = challenge.getChallengeCategory();
        this.challengeProgress = challenge.getChallengeProgress();
        this.challengeImgUrl = challenge.getChallengeImgUrl();
        this.challengeStart = challenge.getChallengeStart();
        this.challengeEnd = challenge.getChallengeEnd();
        this.successPercent = percentProcess(challenge.getChallengeStart(), challenge.getChallengeEnd());
    }

    public int percentProcess(LocalDate startDate, LocalDate endDate){
        LocalDate nowTime = LocalDateTime.now().toLocalDate();
        Long period1 = ChronoUnit.DAYS.between(startDate, endDate);
        Long period2 = ChronoUnit.DAYS.between(startDate, nowTime);

        double mom = (double) period1;
        double son = (double) period2;

        double result = (son / mom) * 100;

        int processTime = (int) result;
        return processTime;
    }
}
