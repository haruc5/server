package com.example.onedaypiece.wep.challenge.controller;

import com.example.onedaypiece.wep.challenge.domain.Challenge;
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

    @PostMapping("/create")
    public String postChallenge(@RequestBody Challenge challenge){
        System.out.println(challenge);
        this.challengeService.postChallenge(challenge);
        return "생성됨";
    }
}
