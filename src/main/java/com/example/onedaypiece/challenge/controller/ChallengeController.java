package com.example.onedaypiece.challenge.controller;

import com.example.onedaypiece.challenge.domain.challenge.Challenge;
import com.example.onedaypiece.challenge.service.ChallengeService;
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
    public String getChallengeDetail(@PathVariable Integer challengeId) {
        System.out.println("challengeId");
        System.out.println(challengeId);
        Challenge challenge = this.challengeService.getChallenge(challengeId);
        // 조회수 기능 구현
        return "challenge";
    }

    @GetMapping("/list")
    @ResponseBody
    public List<Challenge> getChallenge(){
        return this.challengeService.getChallenges();
    }

    @PostMapping("/create")
    public String postChallenge(@RequestBody Challenge challenge){
        System.out.println(challenge);
        this.challengeService.postChallenge(challenge);
        return "생성됨";
    }
}
