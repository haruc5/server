package com.example.onedaypiece.web.challengeDetail.controller;

import com.example.onedaypiece.web.challenge.domain.ChallengeListDto;
import com.example.onedaypiece.web.challenge.service.ChallengeService;
import com.example.onedaypiece.web.challengeDetail.domain.ChallengeDetailDto;
import com.example.onedaypiece.web.challengeDetail.service.ChallengeDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ChallengeDetailController {
    private final ChallengeDetailService challengeDetailService;
    private final ChallengeService challengeService;

    // 챌린지 신청
    @PostMapping("/challenge-request/{challengeId}")
    public String requestChallenge(@RequestBody ChallengeDetailDto challengeDetailDto,
                                   @PathVariable Integer challengeId) {
        challengeService.getChallenge(challengeId);
        challengeDetailService.requestChallenge((challengeDetailDto));
        return "request completed";
    }

    // 챌린지 포기
    @DeleteMapping("/challenge_give_up/{challengeId}")
    public void giveUpChallenge(@PathVariable Integer challengeId) {
        challengeDetailService.giveUpChallenge(challengeId);
    }

    //전체보기
    @GetMapping("/challengeDetail")
    public ChallengeListDto getChallenges() {
        return challengeDetailService.getChallengeBySearch("ALL", "ALL", 0, 0);
    }

    //제목으로 검색
    @GetMapping("/search/{searchWords}")
    public ChallengeListDto getChallengeSearchResult(@PathVariable String searchWords){
        return challengeDetailService.getChallengeSearchResult(searchWords);
    }

    //소팅으로 검색
    @GetMapping("/search/{word}/{challengeCategory}/{period}/{progress}")
    public ChallengeListDto getChallengeSearchByCategoryNameAndPeriod(@PathVariable String word,
                                                                              @PathVariable String challengeCategory,
                                                                              @PathVariable int period,
                                                                              @PathVariable int progress) {
        return challengeDetailService.getChallengeBySearch(word, challengeCategory, period, progress);
    }
}
