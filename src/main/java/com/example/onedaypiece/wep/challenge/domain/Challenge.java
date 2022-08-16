package com.example.onedaypiece.wep.challenge.domain;

import com.example.onedaypiece.wep.challengeDetail.domain.UpdateChallengeDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@Entity
@Data
public class Challenge implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer challengeId;

    @Column(length = 100, nullable = false)
    private String challengeTitle;

    @Column(name = "challenge_category", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ChallengeCategory challengeCategory;

    @Column(length = 3000)
    private String challengeImgUrl;

    @Column
    private String challengeHoliday;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate challengeStart;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate challengeEnd;

    @Column(name = "challenge_status", nullable = false)
    private boolean challengeStatus; // 삭제 여부

    @Column(name = "challenge_progress", nullable = false)
    private Integer challengeProgress;

    @Column(name = "challenge_auth" ,nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ChallengeAuth challengeAuth;

    @Column(length = 100)
    private String challengeAuthMethod;

    @Column(columnDefinition="TEXT")
    private String challengeContent;

    @Column
    private String ChallengePassword;

    @Column(columnDefinition = "integer default 0")
    private Integer viewCount;

    @Column
    private String weekTag;

    @Builder
    public Challenge(String challengeTitle, ChallengeCategory challengeCategory,
                    String challengeImgUrl, String challengeHoliday,
                    LocalDate challengeStart, LocalDate challengeEnd, ChallengeAuth challengeAuth, String challengeAuthMethod,
                    String challengeContent, String ChallengePassword) {
        this.challengeTitle = challengeTitle;
        this.challengeCategory = challengeCategory;
        this.challengeImgUrl = challengeImgUrl;
        this.challengeHoliday = challengeHoliday;
        this.challengeStart = challengeStart;
        this.challengeEnd = challengeEnd;
        this.challengeStatus = true;
        this.challengeProgress = 0;
        this.challengeAuth = challengeAuth;
        this.challengeAuthMethod = challengeAuthMethod;
        this.challengeContent = challengeContent;
        this.ChallengePassword = ChallengePassword;
        this.viewCount = 0;
        if (ChronoUnit.DAYS.between(challengeStart, challengeEnd) <= 7) {
            this.weekTag = "1주";
        } else if (ChronoUnit.DAYS.between(challengeStart, challengeEnd) <= 14) {
            this.weekTag = "2주";
        } else if (ChronoUnit.DAYS.between(challengeStart, challengeEnd) <= 21) {
            this.weekTag = "3주";
        } else {
            this.weekTag = "4주";
        }
    }

    public static Challenge createChallenge(Challenge challenge) {
        return Challenge.builder()
                .challengeTitle(challenge.getChallengeTitle())
                .challengeCategory(challenge.getChallengeCategory())
                .challengeImgUrl(challenge.getChallengeImgUrl())
                .challengeHoliday(challenge.getChallengeHoliday())
                .challengeStart(challenge.getChallengeStart())
                .challengeEnd(challenge.getChallengeEnd())
                .challengeAuth(challenge.getChallengeAuth())
                .challengeAuthMethod(challenge.getChallengeAuthMethod())
                .challengeContent(challenge.getChallengeContent())
                .ChallengePassword(challenge.getChallengePassword())
                .build();
    }

    public void setChallengeStatusFalse() {
        this.challengeStatus = false;
    }

    public void updateChallengeProgress(Integer challengeProgress) {
        this.challengeProgress = challengeProgress;
    }

    public void updateChallenge(UpdateChallengeDto updateChallengeDto) {
        this.challengeTitle = updateChallengeDto.getChallengeTitle();
        this.challengeImgUrl = updateChallengeDto.getChallengeImgUrl();
        this.challengeHoliday = updateChallengeDto.getChallengeHoliday();

    }

}
