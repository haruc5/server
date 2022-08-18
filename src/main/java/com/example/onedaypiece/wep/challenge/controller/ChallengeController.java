package com.example.onedaypiece.wep.challenge.controller;

import com.example.onedaypiece.wep.challenge.domain.Challenge;
import com.example.onedaypiece.wep.challenge.domain.UpdateChallengeDto;
import com.example.onedaypiece.wep.challenge.service.ChallengeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenge")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping("/detail/{challengeId}")
    public Challenge getChallengeDetail(@PathVariable Integer challengeId) {
        return this.challengeService.getChallenge(challengeId);
    }

    @GetMapping("/list")
    public List<Challenge> getChallenges(){
        return this.challengeService.getChallenges();
    }

    // 챌린지 생성
    @PostMapping("/create")
    public String postChallenge(@RequestBody Challenge challenge){
        System.out.println(challenge);
        this.challengeService.postChallenge(challenge);
        return "생성됨";
    }

    // 챌린지 수정
    @PutMapping("/update")
    public String updateChallenge(@RequestBody UpdateChallengeDto updateChallengeDto) {
        challengeService.updateChallenge(updateChallengeDto);
        return "수정됨";
    }

    //챌린지 삭제
    @DeleteMapping("/delete/{challengeId}")
    public String deleteChallenge(@PathVariable Integer challengeId) {
        challengeService.deleteChallenge(challengeId);
        return "삭제됨";
    }
}
