package com.example.onedaypiece.web.challenge.controller;

import com.example.onedaypiece.web.challenge.domain.Challenge;
import com.example.onedaypiece.web.challenge.domain.ChallengeMainDto;
import com.example.onedaypiece.web.challenge.domain.ChallengeSourceDto;
import com.example.onedaypiece.web.challenge.domain.UpdateChallengeDto;
import com.example.onedaypiece.web.challenge.service.ChallengeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenge")
@AllArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping("/list/ALL")
    public List<Challenge> getChallengeList() {
        return this.challengeService.getChallengeList();
    }

    // 챌린지 단건 조회
    @GetMapping("/detail/{challengeId}")
    public Challenge getChallengeDetail(@PathVariable Integer challengeId) {
        return this.challengeService.getChallenge(challengeId);
    }

    // 메인 페이지
    @GetMapping("/main")
    public ChallengeMainDto getMainChallengePage() {
        return challengeService.getMainPage();
    }

    @GetMapping("/main/hot")
    public List<ChallengeSourceDto> getHotChallengePage() {
        return challengeService.getMainPage().getPopular();
    }

    // 챌린지 생성
    @PostMapping("/create")
    public String postChallenge(@RequestBody Challenge challenge){
        System.out.println(challenge);
        this.challengeService.postChallenge(challenge);
        return "created";
    }

    // 챌린지 수정
    @PutMapping("/update")
    public String updateChallenge(@RequestBody UpdateChallengeDto updateChallengeDto) {
        challengeService.updateChallenge(updateChallengeDto);
        return "updated";
    }

    //챌린지 삭제
    @DeleteMapping("/delete/{challengeId}")
    public String deleteChallenge(@PathVariable Integer challengeId) {
        challengeService.deleteChallenge(challengeId);
        return "deleted";
    }
}
