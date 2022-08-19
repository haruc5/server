package com.example.onedaypiece.wep.challengeDetail.controller;

import com.example.onedaypiece.wep.challenge.domain.ChallengeListDto;
import com.example.onedaypiece.wep.challengeDetail.domain.ChallengeDetailDto;
import com.example.onedaypiece.wep.challengeDetail.service.ChallengeDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ChallengeDetailController {
    private final ChallengeDetailService challengeDetailService;

    // 챌린지 신청
    @PostMapping("/challenge-request")
    public void requestChallenge(@RequestBody ChallengeDetailDto challengeDetailDto) {
        challengeDetailService.requestChallenge((challengeDetailDto));
    }

    // 챌린지 포기
    @DeleteMapping("/challenge_give_up/{challengeId}")
    public void giveUpChallenge(@PathVariable Integer challengeId) {
        challengeDetailService.giveUpChallenge(challengeId);
    }

    //전체보기
    @GetMapping("/challengeDetail/{page}")
    public ChallengeListDto getChallenges(@PathVariable int page) {
        return challengeDetailService.getChallengeBySearch("ALL", "ALL", 0, 0, page);
    }

    //제목으로 검색
    @GetMapping("/search/{searchWords}/{page}")
    public ChallengeListDto getChallengeSearchResult(@PathVariable int page,
                                                     @PathVariable String searchWords){
        return challengeDetailService.getChallengeSearchResult(searchWords, page);
    }

    //소팅으로 검색
    @GetMapping("/search/{word}/{categoryName}/{period}/{progress}/{page}")
    public ChallengeListDto getChallengeSearchByCategoryNameAndPeriod(@PathVariable String word,
                                                                              @PathVariable String categoryName,
                                                                              @PathVariable int period,
                                                                              @PathVariable int progress,
                                                                              @PathVariable int page) {
        return challengeDetailService.getChallengeBySearch(word, categoryName, period, progress, page);
    }
}
